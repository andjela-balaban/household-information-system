package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.NarudzbaDataAccess;
import org.unibl.etf.entity.Narudzba;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;


public class NarudzbeSceneController {
	@FXML AnchorPane paneBack;
	@FXML private TableView<Narudzba> narudzbaTabela;
    @FXML private TableColumn<Narudzba, Integer> colId;
    @FXML private TableColumn<Narudzba, LocalDate> colDatum;
    @FXML private TableColumn<Narudzba, String> colStatus;
    @FXML private TableColumn<Narudzba, Integer> colIdPlacanja;
    @FXML private TableColumn<Narudzba, String> colTipPlacanja;
    @FXML private TableColumn<Narudzba, String> colKupac;
    @FXML private TableColumn<Narudzba, Void> colAkcije;
	
	private Stage stage;
	private Scene scene;
	
	private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final NarudzbaDataAccess narudzbaDAO = mysql.getNarudzbaDataAccess();
    private ObservableList<Narudzba> narudzbaObservableList;


    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdNarudzbe()).asObject());
        colDatum.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getDatumNarudzbe())
        );
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus().toString()));
        
        colIdPlacanja.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPlacanje().getIdPlacanja()).asObject());
        
        colTipPlacanja.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPlacanje().getTipPlacanja().toString())
        );
        colKupac.setCellValueFactory(data ->
        		new SimpleStringProperty(data.getValue().getKupac().getOsoba().getIme() + " " + data.getValue().getKupac().getOsoba().getPrezime())
        );

        ucitajNarudzbe();
        dodajAkcioniButtonUkolonu();
    }
    

    private void dodajAkcioniButtonUkolonu() {
        colAkcije.setCellFactory(col -> new TableCell<>() {

            private final Button btn = new Button("...");

            {
                btn.setOnAction(event -> {
                    Narudzba narudzba = getTableView().getItems().get(getIndex());
                    try {
                        prikaziSadrzajNarudzbe(narudzba);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }
    
    
    private void prikaziSadrzajNarudzbe(Narudzba narudzba) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PregledSadrzajaNarudzbeDialog.fxml"));
        Parent root = loader.load();

        PregledSadrzajaNarudzbeDialogController ctrl = loader.getController();
        ctrl.setNarudzba(narudzba);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    private void ucitajNarudzbe() {
        Task<List<Narudzba>> task = new Task<>() {
            @Override
            protected List<Narudzba> call() {
                return narudzbaDAO.narudzbe();
            }
        };

        task.setOnSucceeded(e -> {
            narudzbaObservableList = FXCollections.observableArrayList(task.getValue());
            narudzbaTabela.setItems(narudzbaObservableList);
        });

        task.setOnFailed(e -> new Alert(Alert.AlertType.ERROR, "Greška pri učitavanju narudzbe").showAndWait());

        new Thread(task).start();
    }

    public void osvjeziNarudzbe() {
        ucitajNarudzbe();
    }

    @FXML
    private void openNarudzbaDialog() throws IOException {
        otvoriDialog(null, "Dodaj narudzbu");
    }

    @FXML
    private void openEditDialog() {
    	Narudzba selektovani = narudzbaTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
            try {
                otvoriDialog(selektovani, "Uredi narudzbu");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Nijedna narudzba nije selektovana za uređivanje.");
        }
    }

    private void otvoriDialog(Narudzba narudzba, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NarudzbaDialog.fxml"));
        Parent root = loader.load();

        NarudzbaDialogController controller = loader.getController();
        controller.setNarudzba(narudzba);

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(narudzbaTabela.getScene().getWindow());
        dialogStage.setScene(new Scene(root));
        dialogStage.showAndWait();

        osvjeziNarudzbe();
    }

    @FXML
    private void deleteSelected() {
    	Narudzba selektovani = narudzbaTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
           	Task<Boolean> task = new Task<>() {
               	@Override
               	protected Boolean call() {
               		return narudzbaDAO.obrisiNarudzbu(selektovani.getIdNarudzbe());
               	}
           	};
           	task.setOnSucceeded(e -> {
               	if (task.getValue()) {
                   	narudzbaObservableList.remove(selektovani);
               	} else {
                   	new Alert(Alert.AlertType.ERROR, "Brisanje nije uspjelo.").showAndWait();
               	}
           	});
           	new Thread(task).start();
        } else {
            System.out.println("Nijedna narudzba nije selektovana za brisanje.");
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
