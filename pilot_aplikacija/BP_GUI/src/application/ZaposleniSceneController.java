package application;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.ZaposleniDataAccess;
import org.unibl.etf.entity.Zaposleni;
import javafx.collections.transformation.FilteredList;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ZaposleniSceneController implements Initializable {
	
    @FXML private AnchorPane paneBack;
    @FXML private TableView<Zaposleni> zaposleniTabela;
    @FXML private TableColumn<Zaposleni, Integer> colId;
    @FXML private TableColumn<Zaposleni, String> colIme;
    @FXML private TableColumn<Zaposleni, String> colPrezime;
    @FXML private TableColumn<Zaposleni, String> colFunkcija;
    @FXML private TableColumn<Zaposleni, Double> colPlata;
    @FXML private TextField searchField;
    
    private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private ZaposleniDataAccess zaposleniDAO = mysql.getZaposleniDataAccess();
    private ObservableList<Zaposleni> zaposleniObservableList = FXCollections.observableArrayList();
    
	private Stage stage;
	private Scene scene;
    
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getOsoba().getIdOsobe()).asObject());
        colIme.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOsoba().getIme()));
        colPrezime.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getOsoba().getPrezime()));
        colFunkcija.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFunkcija().toString()));
        colPlata.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPlata()).asObject());

        FilteredList<Zaposleni> filteredData = new FilteredList<>(zaposleniObservableList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase().trim();
            filteredData.setPredicate(z -> filter.isEmpty() ||
                    z.getOsoba().getIme().toLowerCase().contains(filter) ||
                    z.getOsoba().getPrezime().toLowerCase().contains(filter));
        });

        SortedList<Zaposleni> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(zaposleniTabela.comparatorProperty());

        zaposleniTabela.setItems(sortedData);

        ucitajZaposleneAsync();
    }

    private void ucitajZaposleneAsync() {
        Task<List<Zaposleni>> task = new Task<>() {
            @Override
            protected List<Zaposleni> call() {
                return zaposleniDAO.sviZaposleni();
            }
        };

        task.setOnSucceeded(e -> zaposleniObservableList.setAll(task.getValue()));
        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }

    @FXML
    private void openZaposleniDialog() throws IOException {
        otvoriDialog(null, "Dodaj zaposlenog");
    }

    @FXML
    private void openEditDialog() throws IOException {
        Zaposleni selektovani = zaposleniTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
            otvoriDialog(selektovani, "Uredi zaposlenog");
        }
    }

    private void otvoriDialog(Zaposleni zaposleni, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ZaposleniDialog.fxml"));
        Parent root = loader.load();

        ZaposleniDialogController controller = loader.getController();
        controller.setZaposleni(zaposleni);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.showAndWait();

        ucitajZaposleneAsync();
    }

    @FXML
    private void deleteSelected() {
        Zaposleni selektovani = zaposleniTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    zaposleniDAO.obrisiZaposlenog(selektovani.getOsoba().getIdOsobe());
                    return null;
                }
            };
            task.setOnSucceeded(e -> ucitajZaposleneAsync());
            task.setOnFailed(e -> task.getException().printStackTrace());
            new Thread(task).start();
        }
    }
}
