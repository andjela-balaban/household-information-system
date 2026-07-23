package application;

import org.unibl.etf.entity.*;
import org.unibl.etf.dao.*;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;


public class ZaposleniDialogController {

    @FXML private Label labelId;
    @FXML private TextField imeField;
    @FXML private TextField prezimeField;
    @FXML private TextField plataField;
    @FXML private RadioButton radioFunkcijaMuza;
    @FXML private RadioButton radioFunkcijaPrerada;
    @FXML private RadioButton radioFunkcijaIsporuka;

    private Zaposleni zaposleni;
    private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private ZaposleniDataAccess zaposleniDAO = mysql.getZaposleniDataAccess();

    public void setZaposleni(Zaposleni z) {
        this.zaposleni = z;
        if (z != null) {
            labelId.setText(String.valueOf(z.getOsoba().getIdOsobe()));
            imeField.setText(z.getOsoba().getIme());
            prezimeField.setText(z.getOsoba().getPrezime());
            plataField.setText(String.valueOf(z.getPlata()));
            switch (z.getFunkcija()) {
                case MUZA -> radioFunkcijaMuza.setSelected(true);
                case PRERADA -> radioFunkcijaPrerada.setSelected(true);
                case ISPORUKA -> radioFunkcijaIsporuka.setSelected(true);
            }
        } else {
            labelId.setText("auto-increment");
        }
    }

    @FXML
    private void sacuvaj() {
        try {
            String ime = imeField.getText().trim();
            String prezime = prezimeField.getText().trim();
            double plata = Double.parseDouble(plataField.getText().trim());
            ZaposleniFunkcija funkcija = getOdabranaFunkcija();

            if (funkcija == null) {
                prikaziGresku("Odaberite funkciju zaposlenog.");
                return;
            }

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    if (zaposleni == null) {
                        Osoba novaOsoba = new Osoba(0, ime, prezime);
                        Domacinstvo d = AppContext.getInstance().getDomacinstvo();
                        zaposleni = new Zaposleni(novaOsoba, d, funkcija, plata);
                        return zaposleniDAO.dodajZaposlenog(zaposleni);
                    } else {
                        zaposleni.getOsoba().setIme(ime);
                        zaposleni.getOsoba().setPrezime(prezime);
                        zaposleni.setPlata(plata);
                        zaposleni.setFunkcija(funkcija);
                        return zaposleniDAO.azurirajZaposlenog(zaposleni);
                    }
                }
            };

            task.setOnSucceeded(e -> {
                if (!task.getValue()) {
                    prikaziGresku("Operacija nije uspjela.");
                } else {
                    zatvoriDijalog();
                }
            });

            task.setOnFailed(e -> {
                task.getException().printStackTrace();
                prikaziGresku("Greška pri spremanju.");
            });

            new Thread(task).start();

        } catch (NumberFormatException ex) {
            prikaziGresku("Neispravna vrijednost za platu.");
        }
    }

    private ZaposleniFunkcija getOdabranaFunkcija() {
        if (radioFunkcijaMuza.isSelected()) return ZaposleniFunkcija.MUZA;
        if (radioFunkcijaPrerada.isSelected()) return ZaposleniFunkcija.PRERADA;
        if (radioFunkcijaIsporuka.isSelected()) return ZaposleniFunkcija.ISPORUKA;
        return null;
    }

//    private void prikaziGresku(String msg) {
//        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).showAndWait());
//    }
    
    private void prikaziGresku(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).show());
    }


    private void zatvoriDijalog() {
        Stage stage = (Stage) imeField.getScene().getWindow();
        stage.close();
    }
}
