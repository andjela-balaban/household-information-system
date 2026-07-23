package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.Mlijeko;
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

public class MlijekoSceneController {
	 @FXML
	    private AnchorPane paneBack;

	    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private final MlijekoDataAccess mlijekoDAO = mysql.getMlijekoDataAccess();

	    private ObservableList<Mlijeko> mlijekoObservableList;

	    @FXML private TableView<Mlijeko> mlijekoTabela;
	    @FXML private TableColumn<Mlijeko, Integer> colId;
	    @FXML private TableColumn<Mlijeko, LocalDate> colDatum;
	    @FXML private TableColumn<Mlijeko, Double> colKolicina;
	    @FXML private TableColumn<Mlijeko, Integer> colKvalitet;
	    @FXML private TableColumn<Mlijeko, String> colZivotinja;

	    private Stage stage;
	    private Scene scene;

	    @FXML
	    public void initialize() {
	        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdMlijeka()).asObject());
	        colDatum.setCellValueFactory(data ->
	                new SimpleObjectProperty<>(data.getValue().getDatumMuze())
	        );
	        colKolicina.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getKolicina()).asObject());
	        
	        colKvalitet.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getKvalitet()).asObject());
	        
	        colZivotinja.setCellValueFactory(data ->
	                new SimpleStringProperty(data.getValue().getZivotinja().getIme())
	        );

	        ucitajMlijeko();
	    }

	    private void ucitajMlijeko() {
	        Task<List<Mlijeko>> task = new Task<>() {
	            @Override
	            protected List<Mlijeko> call() {
	                return mlijekoDAO.mlijeko();
	            }
	        };

	        task.setOnSucceeded(e -> {
	            mlijekoObservableList = FXCollections.observableArrayList(task.getValue());
	            mlijekoTabela.setItems(mlijekoObservableList);
	        });

	        task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR, "Greška pri učitavanju mlijeka").showAndWait());

	        new Thread(task).start();
	    }

	    public void osvjeziMlijeko() {
	        ucitajMlijeko();
	    }

	    @FXML
	    private void openMlijekoDialog() throws IOException {
	        otvoriDialog(null, "Dodaj mlijeko");
	    }

	    @FXML
	    private void openEditDialog() {
	    	Mlijeko selektovani = mlijekoTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	            try {
	                otvoriDialog(selektovani, "Uredi mlijeko");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("Nijedno mlijeko nije selektovano za uređivanje.");
	        }
	    }

	    private void otvoriDialog(Mlijeko mlijeko, String title) throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("MlijekoDialog.fxml"));
	        Parent root = loader.load();

	        MlijekoDialogController controller = loader.getController();
	        controller.setMlijeko(mlijeko);

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle(title);
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(mlijekoTabela.getScene().getWindow());
	        dialogStage.setScene(new Scene(root));
	        dialogStage.showAndWait();

	        osvjeziMlijeko();
	    }

	    @FXML
	    private void deleteSelected() {
	    	Mlijeko selektovani = mlijekoTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	           	Task<Boolean> task = new Task<>() {
	               	@Override
	               	protected Boolean call() {
	                   	return mlijekoDAO.obrisiMlijeko(selektovani.getIdMlijeka());
	               	}
	           	};
	           	task.setOnSucceeded(e -> {
	               	if (task.getValue()) {
	                   	mlijekoObservableList.remove(selektovani);
	               	} else {
	                   	new Alert(Alert.AlertType.ERROR, "Brisanje nije uspjelo.").showAndWait();
	               	}
	           	});
	           	new Thread(task).start();
	        } else {
	            System.out.println("Nijedno mlijeko nije selektovano za brisanje.");
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
