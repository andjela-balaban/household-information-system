package application;

import java.util.*;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.SadrzajNarudzbeDataAccess;
import org.unibl.etf.entity.Narudzba;
import org.unibl.etf.entity.Proizvod;
import org.unibl.etf.entity.SadrzajNarudzbe;
import org.unibl.etf.entity.TipProizvoda;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SadrzajNarudzbeDialogController {

    @FXML private Button buttonSacuvaj;
    @FXML private TextField labelKolicina;
    @FXML private ChoiceBox<TipProizvoda> tipProizvodaBox;

    private SadrzajNarudzbe trenutnaStavka;
    private Narudzba narudzba;
    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final SadrzajNarudzbeDataAccess sadrzajDAO = mysql.getSadrzajNarudzbeDataAccess();

    public void setNarudzba(Narudzba n) {
        this.narudzba = n;
    }

    public void setTrenutnaStavka(SadrzajNarudzbe sn) {
        this.trenutnaStavka = sn;
        ucitajProizvode();

        if (sn != null) {
            labelKolicina.setText(String.valueOf(sn.getKolicina()));
            tipProizvodaBox.setValue(sn.getProizvod().getTip());
        }
    }

    @FXML
    private void initialize() {
        ucitajProizvode();
    }

    private void ucitajProizvode() {
    	tipProizvodaBox.setItems(FXCollections.observableArrayList(TipProizvoda.values()));
    }

    @FXML
    private void sacuvaj() {
        String kolicinaStr = labelKolicina.getText().trim();
        TipProizvoda tip = tipProizvodaBox.getValue();

        if (kolicinaStr.isEmpty() || tip == null) {
            prikaziGresku("Sva polja su obavezna.");
            return;
        }

        int kolicina;
        try {
            kolicina = Integer.parseInt(kolicinaStr);
        } catch (NumberFormatException e) {
            prikaziGresku("Količina mora biti broj.");
            return;
        }

        List<Proizvod> slobodni = sadrzajDAO.slobodniProizvodiPoTipu(tip);

        if (slobodni.isEmpty()) {
        	prikaziGresku("Trenutno nema proizvoda tipa \"" + tip + "\" na lageru.");
            return;
        }

        Proizvod proizvod = slobodni.get(0); // uzmi prvi porizvod

        if (trenutnaStavka == null) {
            SadrzajNarudzbe novaStavka = new SadrzajNarudzbe(narudzba, proizvod, kolicina);
            trenutnaStavka = novaStavka;
        } 
        else {
            trenutnaStavka.setProizvod(proizvod);
            trenutnaStavka.setKolicina(kolicina);
        }

        zatvoriDijalog();
    }
    
//    private void prikaziGresku(String msg) {
//        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
//    }
    
    private void prikaziGresku(String msg) {
        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, msg).show());
    }


    private void zatvoriDijalog() {
        Stage stage = (Stage) buttonSacuvaj.getScene().getWindow();
        stage.close();
    }
    
    public SadrzajNarudzbe getTrenutnaStavka() {
        return trenutnaStavka;
    }

}
