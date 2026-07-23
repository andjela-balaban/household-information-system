package application;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.ZivotinjaDataAccess;
import org.unibl.etf.entity.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
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

public class ZivotinjeSceneController implements Initializable {

	    @FXML private AnchorPane paneBack;
	    @FXML private TableView<Zivotinja> zivotnjeTabela;
	    @FXML private TableColumn<Zivotinja, Integer> colId;
	    @FXML private TableColumn<Zivotinja, String> colIme;
	    @FXML private TableColumn<Zivotinja, String> colTip;
	    @FXML private TextField searchField;
	    
	    private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private ZivotinjaDataAccess zivotinjeDAO = mysql.getZivotinjaDataAccess();
	    private ObservableList<Zivotinja> zivotinjeObservableList = FXCollections.observableArrayList();
	    
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
	        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdZivotinje()).asObject());
	        colIme.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIme()));
	        colTip.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTip().toString()));

	        FilteredList<Zivotinja> filteredData = new FilteredList<>(zivotinjeObservableList, p -> true);

	        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
	            String filter = newVal.toLowerCase().trim();
	            filteredData.setPredicate(z -> filter.isEmpty() ||
	                    z.getIme().toLowerCase().contains(filter));
	        });

	        SortedList<Zivotinja> sortedData = new SortedList<>(filteredData);
	        sortedData.comparatorProperty().bind(zivotnjeTabela.comparatorProperty());

	        zivotnjeTabela.setItems(sortedData);

	        ucitajZivotinjeAsync();
	    }

	    private void ucitajZivotinjeAsync() {
	        Task<List<Zivotinja>> task = new Task<>() {
	            @Override
	            protected List<Zivotinja> call() {
	                return zivotinjeDAO.sveZivotinje();
	            }
	        };

	        task.setOnSucceeded(e -> zivotinjeObservableList.setAll(task.getValue()));
	        task.setOnFailed(e -> task.getException().printStackTrace());

	        new Thread(task).start();
	    }

	    @FXML
	    private void openZivotinjaDialog() throws IOException {
	        otvoriDialog(null, "Dodaj zivotinju");
	    }

	    @FXML
	    private void openEditDialog() throws IOException {
	    	Zivotinja selektovani = zivotnjeTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	            otvoriDialog(selektovani, "Uredi zivotinju");
	        }
	    }

	    private void otvoriDialog(Zivotinja zivotinja, String title) throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("ZivotinjaDialog.fxml"));
	        Parent root = loader.load();

	        ZivotinjaDialogController controller = loader.getController();
	        controller.setZivotinja(zivotinja);

	        Stage stage = new Stage();
	        stage.setTitle(title);
	        stage.setScene(new Scene(root));
	        stage.showAndWait();

	        ucitajZivotinjeAsync();
	    }

	    @FXML
	    private void deleteSelected() {
	    	Zivotinja selektovani = zivotnjeTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	            Task<Void> task = new Task<>() {
	                @Override
	                protected Void call() {
	                    zivotinjeDAO.obrisiZivotinju(selektovani.getIdZivotinje());
	                    return null;
	                }
	            };
	            task.setOnSucceeded(e -> ucitajZivotinjeAsync());
	            task.setOnFailed(e -> task.getException().printStackTrace());
	            new Thread(task).start();
	        }
	    }
	    
	    @FXML
	    private void prikaziKolicineMlijeka() throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("KolicineMlijekaScene.fxml"));
	        Parent root = loader.load();

	        KolicineMlijekaController controller = loader.getController();
	        controller.setLista(new ArrayList<>(zivotnjeTabela.getItems()));
	        controller.loadData();

	        Stage stage = new Stage();
	        stage.setTitle("Količine mlijeka po životinjama");
	        stage.setScene(new Scene(root));
	        stage.show();
	    }


}
