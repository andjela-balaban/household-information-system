package application;

import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.MlijekoDataAccess;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.JedinicaMjere;
import org.unibl.etf.entity.Mlijeko;
import org.unibl.etf.entity.Zivotinja;
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

public class MlijekoDialogController {
	 private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private final ZivotinjaDataAccess zivotinjaDAO = mysql.getZivotinjaDataAccess();
	    private final MlijekoDataAccess mlijekoDAO = mysql.getMlijekoDataAccess();

	    @FXML private Button buttonSacuvaj;
	    @FXML private Label labelId;
	    @FXML private DatePicker datumMuze;
	    @FXML private TextField labelKolicina;
	    @FXML private TextField labelKvalitet;
	    @FXML private ChoiceBox<String> zivotinjaBox;
	    
	    private Mlijeko mlijeko;

	    public void setMlijeko(Mlijeko m) {
	        this.mlijeko = m;
	        ucitajZivotinje();

	        if (m != null) {
	            labelId.setText(String.valueOf(m.getIdMlijeka()));
	            datumMuze.setValue(m.getDatumMuze());
	            labelKolicina.setText(String.valueOf(m.getKolicina()));
	            labelKvalitet.setText(String.valueOf(m.getKvalitet()));
	            zivotinjaBox.setValue(m.getZivotinja().getIme());
	        } else {
	            labelId.setText("auto-increment");
	        }
	    }

	    private void ucitajZivotinje() {
	        List<Zivotinja> zivotinje = zivotinjaDAO.sveZivotinje();
	        zivotinjaBox.setItems(FXCollections.observableArrayList(
	        		zivotinje.stream().map(Zivotinja::getIme).toList()
	        ));
	    }
	    
	    @FXML
	    private void sacuvaj() {
	        Task<Void> task = new Task<>() {
	            @Override
	            protected Void call() {
	                try {
	                    String kolicinaStr = labelKolicina.getText().trim();
	                    String kvalitetStr = labelKvalitet.getText().trim();
	                    String ime = zivotinjaBox.getValue();

	                    if (kolicinaStr.isEmpty() || kvalitetStr.isEmpty() || ime == null) {
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
	                    
	                    int kvalitet;
	                    try {
	                    	kvalitet = Integer.parseInt(kvalitetStr);
	                    } catch (NumberFormatException e) {
	                        Platform.runLater(() -> prikaziGresku("Kvalitet mora biti broj."));
	                        return null;
	                    }

	                    Zivotinja zivotinja = zivotinjaDAO.sveZivotinje()
	                            .stream()
	                            .filter(z -> z.getIme().equals(ime))
	                            .findFirst()
	                            .orElse(null);

	                    if (zivotinja == null) {
	                        Platform.runLater(() -> prikaziGresku("Zivotinja sa izabranim imenom ne postoji."));
	                        return null;
	                    }
	                    LocalDate datum = datumMuze.getValue();

	                    if (mlijeko == null) {
	                        mlijeko = new Mlijeko(0, datum, kolicina, JedinicaMjere.LITAR, kvalitet, zivotinja);
	                        mlijekoDAO.dodajMlijeko(mlijeko);
	                    } else {
	                        mlijeko.setZivotinja(zivotinja);
	                        mlijeko.setKolicina(kolicina);
	                        mlijeko.setKvalitet(kvalitet);
	                        mlijeko.setDatumMuze(datum);
	                        mlijekoDAO.azurirajMlijeko(mlijeko);
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
