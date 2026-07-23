package application;

import java.util.Map;
import org.unibl.etf.dao.DataAccessFactory;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.SadrzajNarudzbeDataAccess;
import org.unibl.etf.entity.Narudzba;
import org.unibl.etf.entity.Proizvod;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class PregledSadrzajaNarudzbeDialogController {

    @FXML private TableView<Map.Entry<Proizvod, Integer>> tableStavke;
    @FXML private TableColumn<Map.Entry<Proizvod, Integer>, String> colProizvod;
    @FXML private TableColumn<Map.Entry<Proizvod, Integer>, Integer> colKolicina;
    @FXML private TableColumn<Map.Entry<Proizvod, Integer>, Double> colCijena;

    private Narudzba narudzba;

    private final DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
    private final SadrzajNarudzbeDataAccess sadrzajDAO = mysql.getSadrzajNarudzbeDataAccess();

    public void setNarudzba(Narudzba n) {
        this.narudzba = n;
        ucitajStavke();
    }

    @FXML
    private void initialize() {
        colProizvod.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey().getTip().toString()));
        colKolicina.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getValue()).asObject());
        colCijena.setCellValueFactory(data -> {
            double cijenaProizvoda = data.getValue().getKey().getCijena();
            int kolicina = data.getValue().getValue();                   
            double ukupno = cijenaProizvoda * kolicina;                    
            return new SimpleDoubleProperty(ukupno).asObject();              
        });

    }

    private void ucitajStavke() {
        Map<Proizvod, Integer> mapa = sadrzajDAO.proizvodiIzNarudzbe(narudzba.getIdNarudzbe());
        tableStavke.setItems(FXCollections.observableArrayList(mapa.entrySet()));
    }
}
