package application;

import org.unibl.etf.dao.AdresaDataAccess;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.KupacDataAccess;
import org.unibl.etf.dao.mysql.*;
import org.unibl.etf.entity.*;
import java.util.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class KupacDialogController {

    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final TelefonODataAccessImpl telefonODAO = new TelefonODataAccessImpl();
    private final KupacDataAccess kupacDAO = mysql.getKupacDataAccess();
    private final AdresaDataAccess adresaDAO = mysql.getAdresaDataAccess();

    @FXML private Button buttonSacuvaj;
    @FXML private TextField imeField;
    @FXML private Label labelId;
    @FXML private TextField prezimeField;
    @FXML private TextField telefonField;
    @FXML private TextField nazivMjestaField;
    @FXML private TextField postanskiBrojField;
    @FXML private TextField ulicaField;
    @FXML private TextField brojKuceField;

    private Kupac kupac;

    public void setKupac(Kupac k) {
        this.kupac = k;
        if (k != null) {
            labelId.setText(String.valueOf(k.getOsoba().getIdOsobe()));
            imeField.setText(k.getOsoba().getIme());
            prezimeField.setText(k.getOsoba().getPrezime());

            List<TelefonO> telefoni = telefonODAO.telefoniZaOsobu(k.getOsoba().getIdOsobe());
            if (!telefoni.isEmpty()) {
                telefonField.setText(telefoni.get(0).getBrojTelefona());
            }

            if (k.getOsoba().getAdresa() != null) {
                brojKuceField.setText(String.valueOf(k.getOsoba().getAdresa().getKucniBroj()));
                if (k.getOsoba().getAdresa().getUlica() != null) {
                    ulicaField.setText(k.getOsoba().getAdresa().getUlica().getNazivUlice());
                    if (k.getOsoba().getAdresa().getUlica().getMjesto() != null) {
                        nazivMjestaField.setText(k.getOsoba().getAdresa().getUlica().getMjesto().getNaziv());
                        postanskiBrojField.setText(String.valueOf(
                                k.getOsoba().getAdresa().getUlica().getMjesto().getPostanskiBroj()
                        ));
                    }
                }
            }
        } else {
            labelId.setText("auto-increment");
        }
    }

    @FXML
    private void sacuvaj() {
        System.out.println("Kliknuto na Sačuvaj");
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String ime = imeField.getText().trim();
                    String prezime = prezimeField.getText().trim();
                    String brojTelefona = telefonField.getText().trim();
                    String nazivMjesta = nazivMjestaField.getText().trim();
                    String postanskiBrojStr = postanskiBrojField.getText().trim();
                    String nazivUlice = ulicaField.getText().trim();
                    String brojKuceStr = brojKuceField.getText().trim();

                    if (ime.isEmpty() || prezime.isEmpty()) {
                        Platform.runLater(() -> prikaziGresku("Ime i prezime su obavezni."));
                        return null;
                    }

                    Adresa adresa = null;
                    if (!nazivMjesta.isEmpty() && !postanskiBrojStr.isEmpty() &&
                            !nazivUlice.isEmpty() && !brojKuceStr.isEmpty()) {
                        int postanskiBroj = Integer.parseInt(postanskiBrojStr);
                        int brojKuce = Integer.parseInt(brojKuceStr);
                        Mjesto mjesto = new Mjesto(postanskiBroj, nazivMjesta);
                        Ulica ulica = new Ulica(nazivUlice, mjesto);
                        adresa = new Adresa(ulica, brojKuce);

                        adresaDAO.dodajAdresu(adresa);
                        List<Adresa> sveAdrese = adresaDAO.adrese();
                        Adresa dodata = sveAdrese.stream()
                                .filter(a -> a.getUlica().getNazivUlice().equals(nazivUlice) &&
                                        a.getKucniBroj() == brojKuce)
                                .findFirst().orElse(null);
                        if (dodata != null) {
                            adresa.setIdAdrese(dodata.getIdAdrese());
                        }
                    } else if (!nazivMjesta.isEmpty() || !postanskiBrojStr.isEmpty() ||
                            !nazivUlice.isEmpty() || !brojKuceStr.isEmpty()) {
                        Platform.runLater(() -> prikaziGresku("Molimo unesite kompletnu adresu ili ostavite sva polja prazna."));
                        return null;
                    }

                    if (kupac == null) {
                        Osoba novaOsoba = new Osoba(0, ime, prezime, adresa);
                        kupac = new Kupac(novaOsoba);
                        kupacDAO.dodajKupca(kupac);
                        int noviId = kupacDAO.zadnjiIdOsobe();
                        kupac.getOsoba().setIdOsobe(noviId);
                    } else {
                        kupac.getOsoba().setIme(ime);
                        kupac.getOsoba().setPrezime(prezime);
                        kupac.getOsoba().setAdresa(adresa);
                        kupacDAO.azurirajKupca(kupac);
                    }

                    if (!brojTelefona.isEmpty()) {
                        List<TelefonO> postojeći = telefonODAO.telefoniZaOsobu(kupac.getOsoba().getIdOsobe());
                        if (postojeći.isEmpty()) {
                            telefonODAO.dodajTelefon(new TelefonO(kupac.getOsoba(), brojTelefona));
                        } else {
                            TelefonO t = postojeći.get(0);
                            String stariBroj = t.getBrojTelefona();
                            t.setBrojTelefona(brojTelefona);
                            telefonODAO.azurirajTelefon(t, stariBroj);
                        }
                    }

                    Platform.runLater(() -> zatvoriDijalog());

                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> prikaziGresku("Greška pri spremanju podataka."));
                }
                return null;
            }
        };
        new Thread(task).start();
    }

//    private void prikaziGresku(String msg) {
//        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
//    }
    
    private void prikaziGresku(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).show());
    }


    private void zatvoriDijalog() {
        Stage stage = (Stage) imeField.getScene().getWindow();
        stage.close();
    }
}
