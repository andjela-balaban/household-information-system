package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
//import org.unibl.etf.dao.PreradaMlijekaDataAccess;
import org.unibl.etf.dao.ProizvodDataAccess;
import org.unibl.etf.entity.Proizvod;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProizvodiSceneController {

    @FXML private AnchorPane paneBack;

    @FXML private TableView<Proizvod> proizvodiTabela;
    @FXML private TableColumn<Proizvod, Integer> colId;
    @FXML private TableColumn<Proizvod, String> colIdPrerade;
    @FXML private TableColumn<Proizvod, LocalDate> colDatum;
    @FXML private TableColumn<Proizvod, String> colTipProizvoda;
    @FXML private TableColumn<Proizvod, Double> colCijena;

    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final ProizvodDataAccess proizvodDAO = mysql.getProizvodDataAccess();
    private ObservableList<Proizvod> proizvodiObservableList;

    private Stage stage;
    private Scene scene;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdProizvoda()).asObject());
        colIdPrerade.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getPrerada().getIdPrerade()))
        );
        colDatum.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getPrerada().getDatumPrerade())
        );
        colTipProizvoda.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTip().toString())
        );
        colCijena.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCijena()).asObject());

        ucitajProizvode();
    }

    private void ucitajProizvode() {
        Task<List<Proizvod>> task = new Task<>() {
            @Override
            protected List<Proizvod> call() {
                return proizvodDAO.sviProizvodi();
            }
        };

        task.setOnSucceeded(e -> {
            proizvodiObservableList = FXCollections.observableArrayList(task.getValue());
            proizvodiTabela.setItems(proizvodiObservableList);
        });

        task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR, "Greška pri učitavanju proizvoda").showAndWait());

        new Thread(task).start();
    }

    public void osvjeziProizvode() {
        ucitajProizvode();
    }

    @FXML
    private void openProizvodiDialog() throws IOException {
        otvoriDialog(null, "Dodaj proizvod");
    }

    @FXML
    private void openEditDialog() {
        Proizvod selektovani = proizvodiTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
            try {
                otvoriDialog(selektovani, "Uredi proizvod");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nijedan proizvod nije selektovan za uređivanje.");
        }
    }

    private void otvoriDialog(Proizvod proizvod, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProizvodDialog.fxml"));
        Parent root = loader.load();

        ProizvodDialogController controller = loader.getController();
        controller.setProizvod(proizvod);

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(proizvodiTabela.getScene().getWindow());
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        osvjeziProizvode();
    }

    @FXML
    private void deleteSelected() {
        Proizvod selektovani = proizvodiTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
           	Task<Boolean> task = new Task<>() {
               	@Override
               	protected Boolean call() {
                   	return proizvodDAO.obrisiProizvod(selektovani.getIdProizvoda());
               	}
           	};
           	task.setOnSucceeded(e -> {
               	if (task.getValue()) {
                   	proizvodiObservableList.remove(selektovani);
               	} else {
                   	new Alert(Alert.AlertType.ERROR, "Brisanje nije uspjelo.").showAndWait();
               	}
           	});
           	new Thread(task).start();
        } else {
            System.out.println("Nijedan proizvod nije selektovan za brisanje.");
        }
    }

    public void switchToHomeScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SceneHome.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
}


