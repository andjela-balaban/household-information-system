package application;

import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.Isporuka;
import org.unibl.etf.entity.Kupac;
import org.unibl.etf.entity.Zaposleni;
import org.unibl.etf.entity.ZaposleniFunkcija;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class IsporukaDialogController {
	 private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private final ZaposleniDataAccess isporucilacDAO = mysql.getZaposleniDataAccess();
	    private final KupacDataAccess kupacDAO = mysql.getKupacDataAccess();
	    private final IsporukaDataAccess isporukaDAO = mysql.getIsporukaDataAccess();

	    @FXML private Button buttonSacuvaj;
	    @FXML private Label labelId;
	    @FXML private DatePicker datumIsporuke;
	    @FXML private ChoiceBox<String> kupacBox;
	    @FXML private ChoiceBox<String> isporucilacBox;
	    
	    private Isporuka isporuka;

	    public void setIsporuka(Isporuka i) {
	        this.isporuka = i;
	        ucitajKupce();
	        ucitajIsporucioce();

	        if (i != null) {
	            labelId.setText(String.valueOf(i.getIdIsporuke()));
	            datumIsporuke.setValue(i.getDatumIsporuke());
	            kupacBox.setValue(i.getKupac().getOsoba().getIme() + " " + i.getKupac().getOsoba().getPrezime());
	            isporucilacBox.setValue(i.getZaposleniIsporucilac().getOsoba().getIme() + " " + i.getZaposleniIsporucilac().getOsoba().getPrezime());
	        } else {
	            labelId.setText("auto-increment");
	        }
	    }

	    
	    private void ucitajKupce() {
	        List<Kupac> kupci = kupacDAO.kupci();
	        kupacBox.setItems(FXCollections.observableArrayList(
	        		kupci.stream()
	        			.filter(k -> k.getOsoba().getAdresa() != null) // filtriranje
	        			.map(k -> k.getOsoba().getIme() + " " + k.getOsoba().getPrezime()).toList()
	        ));
	    }
	    
	    private void ucitajIsporucioce() {
	        List<Zaposleni> isporucioci = isporucilacDAO.sviZaposleni();
	        isporucilacBox.setItems(FXCollections.observableArrayList(
	        		isporucioci.stream()
	        		  	.filter(i -> i.getFunkcija() == ZaposleniFunkcija.ISPORUKA) // filtriranje
	        			.map(i -> i.getOsoba().getIme() + " " + i.getOsoba().getPrezime()).toList()
	        ));
	    }
	    
	    @FXML
	    private void sacuvaj() {
	        Task<Void> task = new Task<>() {
	            @Override
	            protected Void call() {
	                try {
	                	String kupac = kupacBox.getValue();
	                    String isporucilac = isporucilacBox.getValue();

	                    if (isporucilac == null || isporucilac == null) {
	                        Platform.runLater(() -> prikaziGresku("Sva polja su obavezna."));
	                        return null;
	                    }
	                    
	                    String imeKupac = kupac.split(" ")[0];
	                    String prezimeKupac = kupac.split(" ")[1];
	                    Kupac kupacIsporuke = kupacDAO.kupci()
	                            .stream()
	                            .filter(k -> k.getOsoba().getIme().equals(imeKupac))
	                            .filter(k -> k.getOsoba().getPrezime().equals(prezimeKupac))
	                            .findFirst()
	                            .orElse(null);

	                    if (kupacIsporuke == null) {
	                        Platform.runLater(() -> prikaziGresku("Kupac sa izabranim imenom ne postoji."));
	                        return null;
	                    }
	                    
	                    String imeIsporucilac = isporucilac.split(" ")[0];
	                    String prezimeIsporucilac = isporucilac.split(" ")[1];
	                    Zaposleni zaposleniIsporucilac = isporucilacDAO.sviZaposleni()
	                            .stream()
	                            .filter(z -> z.getOsoba().getIme().equals(imeIsporucilac))
	                            .filter(z -> z.getOsoba().getPrezime().equals(prezimeIsporucilac))
	                            .findFirst()
	                            .orElse(null);

	                    if (zaposleniIsporucilac == null) {
	                        Platform.runLater(() -> prikaziGresku("Isporucilsc sa izabranim imenom ne postoji."));
	                        return null;
	                    }
	                    
	                    LocalDate datum = datumIsporuke.getValue();

	                    if (isporuka == null) {
	                        isporuka = new Isporuka(0, datum, kupacIsporuke.getOsoba().getAdresa(), kupacIsporuke, zaposleniIsporucilac);
	                        isporukaDAO.dodajIsporuku(isporuka);
	                    } else {
	                    	isporuka.setKupac(kupacIsporuke);
	                        isporuka.setZaposleniIsporucilac(zaposleniIsporucilac);
	                        isporuka.setDatumIsporuke(datum);
	                        isporukaDAO.azurirajIsporuku(isporuka);
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
}
