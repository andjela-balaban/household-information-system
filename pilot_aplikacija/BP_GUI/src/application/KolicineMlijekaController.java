package application;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.ZivotinjaDataAccess;
import org.unibl.etf.entity.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class KolicineMlijekaController implements Initializable {

    @FXML private TableView<Zivotinja> tabela;
    @FXML private TableColumn<Zivotinja, Integer> colId;
    @FXML private TableColumn<Zivotinja, String> colIme;
    @FXML private TableColumn<Zivotinja, String> colTip;
    @FXML private TableColumn<Zivotinja, Double> colKolicina;

    private List<Zivotinja> lista;

    private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private ZivotinjaDataAccess zivotinjaDAO = mysql.getZivotinjaDataAccess();

    public void setLista(List<Zivotinja> lista) {
        this.lista = lista;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getIdZivotinje()).asObject());
        colIme.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIme()));
        colTip.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTip().toString()));
        colKolicina.setCellValueFactory(c -> 
             new SimpleDoubleProperty(zivotinjaDAO.kolicinaMlijeka(c.getValue().getIdZivotinje())).asObject());
    }

    public void loadData() {
        tabela.setItems(FXCollections.observableArrayList(lista));
    }
}
