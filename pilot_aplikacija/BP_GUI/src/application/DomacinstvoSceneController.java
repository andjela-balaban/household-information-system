package application;

import java.io.IOException;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.TelefonDDataAccess;
import org.unibl.etf.entity.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DomacinstvoSceneController {
	@FXML private Label jib;
	@FXML private Label naziv;
	@FXML private Label adresa;
	@FXML private Label telefon;
	@FXML private Label imeKontakt;
	@FXML private Label prezimeKontakt;
	@FXML private AnchorPane paneBack;
	
	@FXML private Label dobitLabel;
	
	private Stage stage;
	private Scene scene;

	private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	private TelefonDDataAccess telefonDDAO = mysql.getTelefonDDataAccess();
	
	public void switchToHomeScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneHome.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena home");
	}
	
	@FXML
    public void initialize() {
		Domacinstvo d = AppContext.getInstance().getDomacinstvo();   // Dohvatanje trenutnog domacinstva iz AppContext

        if (d != null) {
            naziv.setText(d.getNaziv());
            jib.setText(d.getJib());
            List<TelefonD> telefoni = telefonDDAO.telefoniZaDomacinstvo(d.getJib());
            if(!telefoni.isEmpty()) {
            	for(TelefonD t : telefoni) {
            		telefon.setText(t.getBrojTelefona() + "\n");
            	}
            }

            // Ako adresa ima ulice i mjesto
            if (d.getAdresa() != null) {
                adresa.setText(d.getAdresa().getUlica().getNazivUlice() + " " +
                                    d.getAdresa().getKucniBroj() + ", " +
                                    d.getAdresa().getUlica().getMjesto().getPostanskiBroj() + " " +  d.getAdresa().getUlica().getMjesto().getNaziv());
            } else {
                adresa.setText(" - ");
            }

            // Kontakt osoba
            if (d.getKontaktOsoba() != null) {
                imeKontakt.setText(d.getKontaktOsoba().getIme());
                prezimeKontakt.setText(d.getKontaktOsoba().getPrezime());
            } else {
            	imeKontakt.setText(" - ");
                prezimeKontakt.setText(" - ");
            }
            double dobit = mysql.getDomacinstvoDataAccess().izracunajDobit(1, d.getJib());
            dobitLabel.setText(String.valueOf(dobit));
        }
    }

}
