package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.*;
import org.unibl.etf.entity.Isporuka;
import javafx.application.Platform;
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

public class IsporukeSceneController {
	 @FXML
	    private AnchorPane paneBack;

	    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	    private final IsporukaDataAccess isporukaDAO = mysql.getIsporukaDataAccess();

	    private ObservableList<Isporuka> isporukaObservableList;

	    @FXML private TableView<Isporuka> isporukeTabela;
	    @FXML private TableColumn<Isporuka, Integer> colId;
	    @FXML private TableColumn<Isporuka, LocalDate> colDatum;
	    @FXML private TableColumn<Isporuka, String> colKupac;
	    @FXML private TableColumn<Isporuka, String> colIsporucilac;

	    private Stage stage;
	    private Scene scene;

	    @FXML
	    public void initialize() {
	        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdIsporuke()).asObject());
	        colDatum.setCellValueFactory(data ->
	                new SimpleObjectProperty<>(data.getValue().getDatumIsporuke())
	        );
	        
	        colKupac.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getKupac().getOsoba().getIme() + " " + data.getValue().getKupac().getOsoba().getPrezime())
    );
	        
	        colIsporucilac.setCellValueFactory(data ->
	                new SimpleStringProperty(data.getValue().getZaposleniIsporucilac().getOsoba().getIme() + " " + data.getValue().getZaposleniIsporucilac().getOsoba().getPrezime())
	        );

	        ucitajIsporuke();
	    }

	    private void ucitajIsporuke() {
	        Task<List<Isporuka>> task = new Task<>() {
	            @Override
	            protected List<Isporuka> call() {
	                return isporukaDAO.isporuke();
	            }
	        };

	        task.setOnSucceeded(e -> {
	            isporukaObservableList = FXCollections.observableArrayList(task.getValue());
	            isporukeTabela.setItems(isporukaObservableList);
	        });

	        task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR, "Greška pri učitavanju isporuke").showAndWait());

	        new Thread(task).start();
	    }

	    public void osvjeziIsporuke() {
	        ucitajIsporuke();
	    }

	    @FXML
	    private void openIsporukaDialog() throws IOException {
	        otvoriDialog(null, "Dodaj isporuku");
	    }

	    @FXML
	    private void openEditDialog() {
	    	Isporuka selektovani = isporukeTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	            try {
	                otvoriDialog(selektovani, "Uredi isporuku");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } else {
	            System.out.println("Nijedna isporuka nije selektovano za uređivanje.");
	        }
	    }

	    private void otvoriDialog(Isporuka isporuka, String title) throws IOException {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("IsporukaDialog.fxml"));
	        Parent root = loader.load();

	        IsporukaDialogController controller = loader.getController();
	        controller.setIsporuka(isporuka);

	        Stage dialogStage = new Stage();
	        dialogStage.setTitle(title);
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(isporukeTabela.getScene().getWindow());
	        dialogStage.setScene(new Scene(root));
	        dialogStage.showAndWait();

	        osvjeziIsporuke();
	    }

	    @FXML
	    private void deleteSelected() {
	    	Isporuka selektovani = isporukeTabela.getSelectionModel().getSelectedItem();
	        if (selektovani != null) {
	           	Task<Boolean> task = new Task<>() {
	               	@Override
	               	protected Boolean call() {
	                   	return isporukaDAO.obrisiIsporuku(selektovani.getIdIsporuke());
	               	}
	           	};
	           	task.setOnSucceeded(e -> {
	               	if (task.getValue()) {
	                   	isporukaObservableList.remove(selektovani);
	               	} else {
	                   	new Alert(Alert.AlertType.ERROR, "Brisanje nije uspjelo.").showAndWait();
	               	}
	           	});
	           	new Thread(task).start();
	        } else {
	            System.out.println("Nijedna isporuka nije selektovana za brisanje.");
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
