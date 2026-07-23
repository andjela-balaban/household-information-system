package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.PreradaMlijeka;
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

public class PreradaMlijekaSceneController {
	 	@FXML private AnchorPane paneBack; 
	    @FXML private TableView<PreradaMlijeka> preradaTabela;
	    @FXML private TableColumn<PreradaMlijeka, Integer> colId;
	    @FXML private TableColumn<PreradaMlijeka, LocalDate> colDatum;
	    @FXML private TableColumn<PreradaMlijeka, LocalDate> colDatumMuze;
	    @FXML private TableColumn<PreradaMlijeka, Double> colKolicina;
	    @FXML private TableColumn<PreradaMlijeka, String> colZaposleni;
	    
	    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private final PreradaMlijekaDataAccess preradaDAO = mysql.getPreradaMlijekaDataAccess();
	    private ObservableList<PreradaMlijeka> preradaObservableList;

	    private Stage stage;
	    private Scene scene;

	    @FXML
	    public void initialize() {
	        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdPrerade()).asObject());
	        colDatum.setCellValueFactory(data ->
	                new SimpleObjectProperty<>(data.getValue().getDatumPrerade())
	        );
	        
	        colDatumMuze.setCellValueFactory(data ->
	        		new SimpleObjectProperty<>(data.getValue().getMlijeko().getDatumMuze())
	        );
	        
	        colKolicina.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getKolicinaZaPreradu()).asObject());
	        
	        colZaposleni.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getZaposleni().getOsoba().getIme() + " " + data.getValue().getZaposleni().getOsoba().getPrezime())
    );

	        ucitajPreradeMlijeka();
	    }

	    private void ucitajPreradeMlijeka() {
	        Task<List<PreradaMlijeka>> task = new Task<>() {
	            @Override
	            protected List<PreradaMlijeka> call() {
	                return preradaDAO.svaPreradaMlijeka();
	            }
	        };

	        task.setOnSucceeded(e -> {
	            preradaObservableList = FXCollections.observableArrayList(task.getValue());
	            preradaTabela.setItems(preradaObservableList);
	        });

	        task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR, "Greška pri učitavanju prerada mlijeka").showAndWait());

	        new Thread(task).start();
	    }

	    public void osvjeziPreradeMlijeka() {
	        ucitajPreradeMlijeka();
	    }

	    @FXML
	    private void openPreradaMlijekaDialog() throws IOException {
	        otvoriDialog(null, "Dodaj preradu mlijeka");
	    }

	    @FXML
	    private void openEditDialog() {
	    	PreradaMlijeka selektovani = preradaTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	            try {
	                otvoriDialog(selektovani, "Uredi preradu mlijeka");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("Nijedna prerada mlijeka nije selektovana za uređivanje.");
	        }
	    }

	    private void otvoriDialog(PreradaMlijeka preradaMlijeka, String title) throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("PreradaMlijekaDialog.fxml"));
	        Parent root = loader.load();

	        PreradaMlijekaDialogController controller = loader.getController();
	        controller.setPreraduMlijeka(preradaMlijeka);

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle(title);
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(preradaTabela.getScene().getWindow());
	        dialogStage.setScene(new Scene(root));
	        dialogStage.showAndWait();

	        osvjeziPreradeMlijeka();
	    }

	    @FXML
	    private void deleteSelected() {
	    	PreradaMlijeka selektovani = preradaTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	           	Task<Boolean> task = new Task<>() {
	               	@Override
	               	protected Boolean call() {
	                   	return preradaDAO.obrisiPreraduMlijeka(selektovani.getIdPrerade());
	               	}
	           	};
	           	task.setOnSucceeded(e -> {
	               	if (task.getValue()) {
	                   	preradaObservableList.remove(selektovani);
	               	} else {
	                   	new Alert(Alert.AlertType.ERROR, "Brisanje nije uspjelo.").showAndWait();
	               	}
	           	});
	           	new Thread(task).start();
	        } else {
	            System.out.println("Nijedna prerada mlijeka nije selektovana za brisanje.");
	        }
	    }

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
}
