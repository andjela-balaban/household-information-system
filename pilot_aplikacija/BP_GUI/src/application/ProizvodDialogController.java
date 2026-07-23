package application;

import java.time.LocalDate;

import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.PreradaMlijekaDataAccess;
import org.unibl.etf.dao.ProizvodDataAccess;
import org.unibl.etf.entity.*;
import org.unibl.etf.entity.TipProizvoda;
import java.util.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class ProizvodDialogController {

    @FXML private Button buttonSacuvaj;
    @FXML private Label labelId;
    @FXML private Label labelPreradaId;
    @FXML private ChoiceBox<LocalDate> datumPreradeBox;
    @FXML private ChoiceBox<TipProizvoda> tipProizvodaBox;
    @FXML private TextField cijenaField;

    private Proizvod proizvod;
    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final PreradaMlijekaDataAccess preradaDAO = mysql.getPreradaMlijekaDataAccess();
    private final ProizvodDataAccess proizvodDAO = mysql.getProizvodDataAccess();

    public void setProizvod(Proizvod p) {
        this.proizvod = p;
        ucitajPrerade();
        ucitajTipoveProizvoda();

        if (p != null) {
            labelId.setText(String.valueOf(p.getIdProizvoda()));
            labelPreradaId.setText(String.valueOf(p.getPrerada().getIdPrerade()));
            datumPreradeBox.setValue(p.getPrerada().getDatumPrerade());
            tipProizvodaBox.setValue(p.getTip());
            cijenaField.setText(String.valueOf(p.getCijena()));
        } else {
            labelId.setText("auto-increment");
        }
    }

    private void ucitajPrerade() {
        List<PreradaMlijeka> prerade = preradaDAO.svaPreradaMlijeka();
        datumPreradeBox.setItems(FXCollections.observableArrayList(
                prerade.stream().map(PreradaMlijeka::getDatumPrerade).toList()
        ));
    }

    private void ucitajTipoveProizvoda() {
        tipProizvodaBox.setItems(FXCollections.observableArrayList(TipProizvoda.values()));
    }
    
    @FXML
    private void sacuvaj() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    LocalDate datumPrerade = datumPreradeBox.getValue();
                    TipProizvoda tip = tipProizvodaBox.getValue();
                    String cijenaStr = cijenaField.getText().trim();

                    if (datumPrerade == null || tip == null || cijenaStr.isEmpty()) {
                        Platform.runLater(() -> prikaziGresku("Sva polja su obavezna."));
                        return null;
                    }

                    double cijena;
                    try {
                        cijena = Double.parseDouble(cijenaStr);
                    } catch (NumberFormatException e) {
                        Platform.runLater(() -> prikaziGresku("Cijena mora biti broj."));
                        return null;
                    }

                    PreradaMlijeka prerada = preradaDAO.svaPreradaMlijeka()
                            .stream()
                            .filter(p -> p.getDatumPrerade().equals(datumPrerade))
                            .findFirst()
                            .orElse(null);

                    if (prerada == null) {
                        Platform.runLater(() -> prikaziGresku("Prerada za odabrani datum ne postoji."));
                        return null;
                    }

                    if (proizvod == null) {
                        proizvod = new Proizvod(0, prerada, tip, cijena);
                        proizvodDAO.dodajProizvod(proizvod);
                    } else {
                        proizvod.setPrerada(prerada);
                        proizvod.setTip(tip);
                        proizvod.setCijena(cijena);
                        proizvodDAO.azurirajProizvod(proizvod);
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
        Stage stage = (Stage) buttonSacuvaj.getScene().getWindow();
        stage.close();
    }
}
