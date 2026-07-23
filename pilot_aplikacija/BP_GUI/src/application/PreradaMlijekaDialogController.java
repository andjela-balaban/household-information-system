package application;

import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.MlijekoDataAccess;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.Domacinstvo;
import org.unibl.etf.entity.JedinicaMjere;
import org.unibl.etf.entity.Mlijeko;
import org.unibl.etf.entity.PreradaMlijeka;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PreradaMlijekaDialogController {
	 private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private final ZaposleniDataAccess zaposleniDAO = mysql.getZaposleniDataAccess();
	    private final PreradaMlijekaDataAccess preradaDAO = mysql.getPreradaMlijekaDataAccess();
	    private final MlijekoDataAccess mlijekoDAO = mysql.getMlijekoDataAccess();

	    @FXML private Button buttonSacuvaj;
	    @FXML private Label labelId;
	    @FXML private DatePicker datumPrerade;
	    @FXML private ChoiceBox<LocalDate> datumMuzeBox;
	    @FXML private TextField labelKolicina;
	    @FXML private ChoiceBox<String> zaposleniBox;
	    
	    private PreradaMlijeka prerada;

	    public void setPreraduMlijeka(PreradaMlijeka m) {
	        this.prerada = m;
	        ucitajMlijeka();
	        ucitajZaposlene();

	        if (m != null) {
	            labelId.setText(String.valueOf(m.getIdPrerade()));
	            datumPrerade.setValue(m.getDatumPrerade());
	            datumMuzeBox.setValue(m.getMlijeko().getDatumMuze());
	            labelKolicina.setText(String.valueOf(m.getKolicinaZaPreradu()));
	            zaposleniBox.setValue(m.getZaposleni().getOsoba().getIme() + " " + m.getZaposleni().getOsoba().getPrezime());
	        } else {
	            labelId.setText("auto-increment");
	        }
	    }

	    private void ucitajZaposlene() {
	        List<Zaposleni> zaposleni = zaposleniDAO.sviZaposleni();
	        zaposleniBox.setItems(FXCollections.observableArrayList(
	        		zaposleni.stream()
        		  	.filter(z -> z.getFunkcija() == ZaposleniFunkcija.PRERADA) // filtriranje
        			.map(z -> z.getOsoba().getIme() + " " + z.getOsoba().getPrezime()).toList()
	        ));
	    }
	    
	    private void ucitajMlijeka() {
	        List<Mlijeko> mlijeka = mlijekoDAO.mlijeko();
	        datumMuzeBox.setItems(FXCollections.observableArrayList(
	                mlijeka.stream().map(Mlijeko::getDatumMuze).toList()
	        ));
	    }
	    
	    @FXML
	    private void sacuvaj() {
	        Task<Void> task = new Task<>() {
	            @Override
	            protected Void call() {
	                try {
	                    String kolicinaStr = labelKolicina.getText().trim();
	                    String preradjivac = zaposleniBox.getValue();
	                    LocalDate datumMuze = datumMuzeBox.getValue();

	                    if (kolicinaStr.isEmpty() || preradjivac == null || datumMuze == null) {
	                        Platform.runLater(() -> prikaziGresku("Sva polja su obavezna."));
	                        return null;
	                    }

	                    double kolicina;
	                    try {
	                        kolicina = Double.parseDouble(kolicinaStr);
	                    } catch (NumberFormatException e) {
	                        Platform.runLater(() -> prikaziGresku("Kolicina mora biti broj."));
	                        return null;
	                    }

	                    String imePreradjivac = preradjivac.split(" ")[0];
	                    String prezimePreradjivac = preradjivac.split(" ")[1];
	                    Zaposleni zaposleniPreradjivac = zaposleniDAO.sviZaposleni()
	                            .stream()
	                            .filter(z -> z.getOsoba().getIme().equals(imePreradjivac))
	                            .filter(z -> z.getOsoba().getPrezime().equals(prezimePreradjivac))
	                            .findFirst()
	                            .orElse(null);

	                    if (zaposleniPreradjivac == null) {
	                        Platform.runLater(() -> prikaziGresku("Preradjivac sa izabranim imenom ne postoji."));
	                        return null;
	                    }
	                    
	                    Mlijeko mlijeko = mlijekoDAO.mlijeko()
	                            .stream()
	                            .filter(m -> m.getDatumMuze().equals(datumMuze))
	                            .findFirst()
	                            .orElse(null);

	                    if (mlijeko == null) {
	                        Platform.runLater(() -> prikaziGresku("Mlijeko za odabrani datum muze ne postoji."));
	                        return null;
	                    }
	                    
	                    LocalDate datum = datumPrerade.getValue();

	                    if (prerada == null) {
	                    	Domacinstvo d = AppContext.getInstance().getDomacinstvo();
	                        prerada = new PreradaMlijeka(0, mlijeko, d, zaposleniPreradjivac, datum, kolicina, JedinicaMjere.LITAR);
	                        preradaDAO.dodajPreraduMlijeka(prerada);
	                    } else {
	                    	prerada.setDatumPrerade(datum);
	                        prerada.setZaposleni(zaposleniPreradjivac);
	                        prerada.setKolicinaZaPreradu(kolicina);
	                        prerada.getMlijeko().setDatumMuze(datumMuze);
	                        preradaDAO.azurirajPreraduMlijeka(prerada);
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
