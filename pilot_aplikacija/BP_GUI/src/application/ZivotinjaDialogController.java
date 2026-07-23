package application;

import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.ZivotinjaDataAccess;
import org.unibl.etf.entity.Domacinstvo;
import org.unibl.etf.entity.*;
import org.unibl.etf.entity.Zivotinja;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ZivotinjaDialogController {

    @FXML private Label labelId;
    @FXML private TextField imeField;
    @FXML private RadioButton radioTipKrava;
    @FXML private RadioButton radioTipKoza;
    
    private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private ZivotinjaDataAccess zivotinjaDAO = mysql.getZivotinjaDataAccess();
    private Zivotinja zivotinja;

    public void setZivotinja(Zivotinja z) {
        this.zivotinja = z;
        if (z != null) {
            labelId.setText(String.valueOf(z.getIdZivotinje()));
            imeField.setText(z.getIme());
            switch (z.getTip()) {
                case KRAVA -> radioTipKrava.setSelected(true);
                case KOZA -> radioTipKoza.setSelected(true);
            }
        } else {
            labelId.setText("auto-increment");
        }
    }

    @FXML
    private void sacuvaj() {
        try {
            String ime = imeField.getText().trim();
            TipZivotinje tip = getOdabraniTip();

            if (tip == null) {
                prikaziGresku("Odaberite tip zivotinje.");
                return;
            }

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() {
                    if (zivotinja == null) {
                        Domacinstvo d = AppContext.getInstance().getDomacinstvo();
                        zivotinja = new Zivotinja(ime, tip, d);
                        return zivotinjaDAO.dodajZivotinju(zivotinja);
                    } else {
                        zivotinja.setIme(ime);
                        zivotinja.setTip(tip);
                        return zivotinjaDAO.azurirajZivotinju(zivotinja);
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

    private TipZivotinje getOdabraniTip() {
        if (radioTipKrava.isSelected()) return TipZivotinje.KRAVA;
        if (radioTipKoza.isSelected()) return TipZivotinje.KOZA;
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
