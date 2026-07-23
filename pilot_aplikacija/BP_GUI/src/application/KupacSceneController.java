package application;

import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.KupacDataAccess;
import org.unibl.etf.dao.mysql.TelefonODataAccessImpl;
import org.unibl.etf.entity.Adresa;
import org.unibl.etf.entity.Kupac;
import org.unibl.etf.entity.TelefonO;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class KupacSceneController implements Initializable {

    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final TelefonODataAccessImpl telefonODAO = new TelefonODataAccessImpl();
    private final KupacDataAccess kupacDAO = mysql.getKupacDataAccess();

    private final ObservableList<Kupac> kupacObservableList = FXCollections.observableArrayList();
    private final Map<Integer, String> telefoniMapa = new HashMap<>();

    private boolean kupciUcitani = false;

    @FXML private TableView<Kupac> kupacTabela;
    @FXML private TableColumn<Kupac, Integer> colId;
    @FXML private TableColumn<Kupac, String> colIme;
    @FXML private TableColumn<Kupac, String> colPrezime;
    @FXML private TableColumn<Kupac, String> colAdresa;
    @FXML private TableColumn<Kupac, String> colTelefon;
    @FXML private TextField searchField;

    private Stage stage;
    private Scene scene;

    public void switchToHomeScene(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SceneHome.fxml"));
        stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
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
        colAdresa.setCellValueFactory(c -> {
            Adresa adr = c.getValue().getOsoba().getAdresa();
            if (adr != null) {
                return new SimpleStringProperty(
                        adr.getUlica().getNazivUlice() + " " + adr.getKucniBroj() + ", " +
                        adr.getUlica().getMjesto().getPostanskiBroj() + " " +
                        adr.getUlica().getMjesto().getNaziv()
                );
            } else {
                return new SimpleStringProperty(" - ");
            }
        });
        colTelefon.setCellValueFactory(c -> {
            String tel = telefoniMapa.get(c.getValue().getOsoba().getIdOsobe());
            return new SimpleStringProperty(tel != null ? tel : " - ");
        });

        FilteredList<Kupac> filteredData = new FilteredList<>(kupacObservableList, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase().trim();
            filteredData.setPredicate(k -> filter.isEmpty() ||
                    k.getOsoba().getIme().toLowerCase().contains(filter) ||
                    k.getOsoba().getPrezime().toLowerCase().contains(filter));
        });

        SortedList<Kupac> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(kupacTabela.comparatorProperty());
        kupacTabela.setItems(sortedData);

        kupacTabela.getSortOrder().add(colId);
        colId.setSortType(TableColumn.SortType.ASCENDING);

        // Ucitavamo kupce samo prvi put
        ucitajKupceAkoTreba();
    }

    private void ucitajKupceAkoTreba() {
        if (!kupciUcitani) {
            ucitajKupceAsync();
            kupciUcitani = true;
        }
    }

    private void ucitajKupceAsync() {
        System.out.println("Učitavam kupce...");
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    List<Kupac> lista = kupacDAO.kupci();
                    List<TelefonO> sviTelefoni = telefonODAO.sviTelefoni();

                    Map<Integer, String> novaMapa = sviTelefoni.stream()
                            .collect(Collectors.groupingBy(
                                    t -> t.getOsoba().getIdOsobe(),
                                    Collectors.mapping(TelefonO::getBrojTelefona, Collectors.joining(", "))
                            ));

                    Platform.runLater(() -> {
                        kupacObservableList.setAll(lista);
                        telefoniMapa.clear();
                        telefoniMapa.putAll(novaMapa);
                        System.out.println("Kupci učitani: " + lista.size());
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    public void osvjeziKupce() {
        kupciUcitani = false;
        ucitajKupceAkoTreba();
    }

    @FXML
    private void openKupacDialog() throws IOException {
        otvoriDialog(null, "Dodaj kupca");
    }

    @FXML
    private void openEditDialog() throws IOException {
        Kupac selektovani = kupacTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
            otvoriDialog(selektovani, "Uredi kupca");
        } else {
            System.out.println("Nijedan kupac nije selektovan za uređivanje.");
        }
    }

    private void otvoriDialog(Kupac kupac, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("KupacDialog.fxml"));
        Parent root = loader.load();

        KupacDialogController controller = loader.getController();
        controller.setKupac(kupac);

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.showAndWait();

        // osvjezvanje prikaza nakon dodavanja/izmjene
        osvjeziKupce();
    }

    @FXML
    private void deleteSelected() {
        Kupac selektovani = kupacTabela.getSelectionModel().getSelectedItem();
        if (selektovani != null) {
            System.out.println("Brišem kupca ID: " + selektovani.getOsoba().getIdOsobe());
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    kupacDAO.obrisiKupca(selektovani.getOsoba().getIdOsobe());
                    return null;
                }
                @Override
                protected void succeeded() {
                    osvjeziKupce();
                }
            };
            new Thread(task).start();
        } else {
            System.out.println("Nijedan kupac nije selektovan za brisanje.");
        }
    }
}