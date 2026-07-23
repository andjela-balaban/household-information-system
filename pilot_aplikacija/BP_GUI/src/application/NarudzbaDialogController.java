package application;

import java.time.LocalDate;
import java.util.List;
import java.util.*;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.IsporukaDataAccess;
import org.unibl.etf.dao.KupacDataAccess;
import org.unibl.etf.dao.NarudzbaDataAccess;
import org.unibl.etf.dao.PlacanjeDataAccess;
import org.unibl.etf.dao.SadrzajNarudzbeDataAccess;
import org.unibl.etf.entity.Isporuka;
import org.unibl.etf.entity.*;
import org.unibl.etf.entity.Narudzba;
import org.unibl.etf.entity.SadrzajNarudzbe;
import org.unibl.etf.entity.StatusNarudzbe;
import org.unibl.etf.entity.TipPlacanja;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;

public class NarudzbaDialogController {
	 private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	 private final KupacDataAccess kupacDAO = mysql.getKupacDataAccess();
	 private final NarudzbaDataAccess narudzbaDAO = mysql.getNarudzbaDataAccess();
	 private final SadrzajNarudzbeDataAccess sadrzajDAO = mysql.getSadrzajNarudzbeDataAccess();
	 private final IsporukaDataAccess isporukaDAO = mysql.getIsporukaDataAccess();
	 private final PlacanjeDataAccess placanjeDAO = mysql.getPlacanjeDataAccess();
	 
	 @FXML private Button buttonSacuvaj;
	 @FXML private Label labelId;
	 @FXML private DatePicker datumNarudzbe;
	 @FXML private ChoiceBox<StatusNarudzbe> statusBox;
	 @FXML private ChoiceBox<String> kupacBox;
	 @FXML private ChoiceBox<TipPlacanja> tipPlacanjaBox;
	 @FXML private ListView<SadrzajNarudzbe> sadrzajNarudzbe;
	 @FXML private Button buttonDodajStavku;   
	 private Narudzba narudzba;
	 
	 
	 public void setNarudzba(Narudzba n) {
		    this.narudzba = n;
		    ucitajKupce();
		    ucitajStatus();
		    ucitajTipPlacanja();

		    if (n != null) {
		        labelId.setText(String.valueOf(n.getIdNarudzbe()));
		        datumNarudzbe.setValue(n.getDatumNarudzbe());
		        statusBox.setValue(n.getStatus());
		        tipPlacanjaBox.setValue(n.getPlacanje().getTipPlacanja());
		        kupacBox.setValue(n.getKupac().getOsoba().getIme() + " " + n.getKupac().getOsoba().getPrezime());

		        // ucitavanje proizvoda iz narudzbe ukoliko postoje
		        Map<Proizvod, Integer> stavkeMap = sadrzajDAO.proizvodiIzNarudzbe(n.getIdNarudzbe());
		        if (stavkeMap != null) {
		            List<SadrzajNarudzbe> stavke = stavkeMap.entrySet()
		                    .stream()
		                    .map(e -> new SadrzajNarudzbe(n, e.getKey(), e.getValue()))
		                    .toList();
		            sadrzajNarudzbe.setItems(FXCollections.observableArrayList(stavke));
		        }

		    } else {
		        labelId.setText("auto-increment");
		        sadrzajNarudzbe.setItems(FXCollections.observableArrayList()); 
		    }
		}



	 	private void ucitajKupce() {
	        List<Kupac> kupci = kupacDAO.kupci();
	        kupacBox.setItems(FXCollections.observableArrayList(
	        		kupci.stream()
	        			.map(k -> k.getOsoba().getIme() + " " + k.getOsoba().getPrezime()).toList()
	        ));
	    }
	    
	    private void ucitajStatus() {
	    	statusBox.setItems(FXCollections.observableArrayList(StatusNarudzbe.values()));
	    }
	    
	    private void ucitajTipPlacanja() {
	    	tipPlacanjaBox.setItems(FXCollections.observableArrayList(TipPlacanja.values()));
	    }
	    
	    @FXML
	    private void sacuvaj() {
	        Task<Void> task = new Task<>() {
	            @Override
	            protected Void call() {
	                try {
	                    String kupacIzabrani = kupacBox.getValue();
	                    StatusNarudzbe status = statusBox.getValue();
	                    TipPlacanja placanjeTip = tipPlacanjaBox.getValue();
	                    LocalDate datum = datumNarudzbe.getValue();

	                    if (placanjeTip == null || kupacIzabrani == null || datum == null) {
	                        Platform.runLater(() -> prikaziGresku("Polja za unos kupca, tipa plaćanja i datuma su obavezna."));
	                        return null;
	                    }

	                    String[] imePrezime = kupacIzabrani.split(" ");
	                    if (imePrezime.length < 2) {
	                        Platform.runLater(() -> prikaziGresku("Kupac mora imati ime i prezime."));
	                        return null;
	                    }
	                    String imeKupac = imePrezime[0];
	                    String prezimeKupac = imePrezime[1];

	                    Kupac kupac = kupacDAO.kupci()
	                            .stream()
	                            .filter(k -> k.getOsoba().getIme().equals(imeKupac) &&
	                                         k.getOsoba().getPrezime().equals(prezimeKupac))
	                            .findFirst()
	                            .orElse(null);

	                    if (kupac == null) {
	                        Platform.runLater(() -> prikaziGresku("Kupac sa izabranim imenom ne postoji."));
	                        return null;
	                    }

	                    // dohvatanje isporuke ukoliko postoji
	                    Isporuka isporuka = null;
	                    if (kupac.getOsoba().getAdresa() != null) {
	                        isporuka = isporukaDAO.isporuke()
	                                .stream()
	                                .filter(i -> i.getKupac().equals(kupac))
	                                .findFirst()
	                                .orElse(null);
	                    }

	                    // racunanje stavki narudzbe
	                    double iznos = sadrzajNarudzbe.getItems()
	                            .stream()
	                            .mapToDouble(sn -> sn.getProizvod().getCijena() * sn.getKolicina())
	                            .sum();

	                    if (narudzba == null) {
	                    	// kreiranje novog placanja
	                        Placanje placanje = null;
	                        placanje = new Placanje(0, kupac, 0.0, placanjeTip);
	                        boolean placanjeDodano = placanjeDAO.dodajPlacanje(placanje);
	                        placanje = placanjeDAO.svaPlacanja()
	                        			.stream()
	                        			.filter(p -> p.getIznos() == 0)
	                        			.findFirst()
	                        			.orElse(null);
	                        if (!placanjeDodano || placanje.getIdPlacanja() == 0) {
	                        	Platform.runLater(() -> prikaziGresku("Greška pri dodavanju plaćanja."));
	                            return null;
	                        }
	                       

	                        // kreiramo novu narudzbu
	                        narudzba = new Narudzba(0, status, datum, kupac, placanje, isporuka);
	                        boolean narudzbaDodana = narudzbaDAO.dodajNarudzbu(narudzba);
	                        if (!narudzbaDodana) {
	                            Platform.runLater(() -> prikaziGresku("Greška pri dodavanju narudžbe."));
	                            return null;
	                        }

	                        // dodajemo stavke iz narudzbe i azuiraramo iznos kod placanja
	                        double noviIznos = 0;
	                        for (SadrzajNarudzbe sn : sadrzajNarudzbe.getItems()) {
	                            sn.setNarudzba(narudzba);
	                            sadrzajDAO.dodajSadrzajNarudzbe(sn);
	                            noviIznos += sn.getProizvod().getCijena() * sn.getKolicina();
	                        }
	                        placanje.setIznos(noviIznos);
	                        placanjeDAO.azurirajPlacanje(placanje);

	                    } 
	                    else {
	                    	// narudzba vec postoji - treba je azurirati
	                        Placanje placanje = narudzba.getPlacanje();
	                        placanje.setTipPlacanja(placanjeTip);
	                        placanje.setIznos(iznos);
	                        placanjeDAO.azurirajPlacanje(placanje);

	                        narudzba.setKupac(kupac);
	                        narudzba.setStatus(status);
	                        narudzba.setDatumNarudzbe(datum);
	                        narudzba.setIsporuka(isporuka);
	                        narudzbaDAO.azurirajNarudzbu(narudzba);

	                        for (SadrzajNarudzbe sn : sadrzajNarudzbe.getItems()) {
	                            sn.setNarudzba(narudzba);
	                            SadrzajNarudzbe postojeci = sadrzajDAO.sadrzajNarudzbe(
	                                    narudzba.getIdNarudzbe(),
	                                    sn.getProizvod().getIdProizvoda()
	                            );
	                            if (postojeci == null) {
	                                sadrzajDAO.dodajSadrzajNarudzbe(sn);
	                            } else {
	                                postojeci.setKolicina(sn.getKolicina());
	                                sadrzajDAO.azurirajSadrzajNarudzbe(postojeci);
	                            }
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

	    
	    
//	    private void prikaziGresku(String msg) {
//	        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
//	    }
	    
	    private void prikaziGresku(String msg) {
	        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).show());
	    }


	    private void zatvoriDijalog() {
	        Stage stage = (Stage) buttonSacuvaj.getScene().getWindow();
	        stage.close();
	    }

	    @FXML
	    private void dodajStavku() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("SadrzajNarudzbeDialog.fxml"));
	            Parent root = loader.load();

	            SadrzajNarudzbeDialogController controller = loader.getController();
	            controller.setNarudzba(narudzba); // postavljanje trenutne narudzbe u kontroler za sadrzaj narudzbe

	            Stage stage = new Stage();
	            stage.setTitle("Dodaj stavku narudžbe");
	            stage.setScene(new Scene(root));
	            stage.initModality(Modality.APPLICATION_MODAL);
	            stage.showAndWait(); 
	            
	            // kada korisnik zatvori dialog - dohvatamo novu izabranu stavku, tj. proizvod 
	            SadrzajNarudzbe novaStavka = controller.getTrenutnaStavka();
	            if (novaStavka != null) {
	                sadrzajNarudzbe.getItems().add(novaStavka);	// dodavanje nove stavke u list view
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	            prikaziGresku("Greška pri otvaranju dijaloga za dodavanje stavki.");
	        }
	    }


}
