package application;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import org.unibl.etf.dao.*;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.mysql.TopProizvodDTO;
import org.unibl.etf.entity.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;



public class TopProizvodiController implements Initializable {

    @FXML private PieChart pieChart;

    private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	private DomacinstvoDataAccess domacinstvoDAO = mysql.getDomacinstvoDataAccess();
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {

	    Domacinstvo d = AppContext.getInstance().getDomacinstvo();
	    if (d == null) 
	        return;

	    List<TopProizvodDTO> lista = domacinstvoDAO.topProizvodi(d.getJib(), 3); // prikaz top 3 proizvoda u pie chart-u

	    ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();

	    for (TopProizvodDTO p : lista) {
	        PieChart.Data data = new PieChart.Data(p.getNaziv(), p.getUkupnoKomada());
	        chartData.add(data);
	    }

	    pieChart.setData(chartData);
	    pieChart.setTitle("Top proizvodi po količini prodaje");

	    Platform.runLater(() -> {
	        for (PieChart.Data data : pieChart.getData()) {
	            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int)data.getPieValue() + " komada");
	            Tooltip.install(data.getNode(), tooltip);
	        }
	    });
	}
	
}
