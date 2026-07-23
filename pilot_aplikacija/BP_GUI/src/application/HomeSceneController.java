package application;

import java.io.IOException;

import org.unibl.etf.dao.*;
import org.unibl.etf.dao.DataAccessFactoryType;
import org.unibl.etf.dao.DomacinstvoDataAccess;
import org.unibl.etf.entity.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeSceneController {
	
	@FXML Button startBtn;
	@FXML Button btnTopProizvodi;
	@FXML AnchorPane paneBack;
	@FXML AnchorPane paneZaposleni;
	@FXML AnchorPane paneKupci;
	@FXML AnchorPane paneProizvodi;
	@FXML AnchorPane paneNarudzbe;
	@FXML AnchorPane paneIsporuke;
	@FXML AnchorPane paneMlijeko;
	@FXML AnchorPane panePreradaMlijeka;
	@FXML AnchorPane paneZivotinje;
	@FXML AnchorPane paneDomacinstvo;

	private Stage stage;
	private Scene scene;
	private static String JIB = "1234567890123";
	
	private DataAccessFactory mysql = DataAccessFactory.getFactory(DataAccessFactoryType.MySQL);
	
	@FXML
	public void initialize() {
	    DomacinstvoDataAccess domacinstvoDAO = mysql.getDomacinstvoDataAccess();
	    Domacinstvo d = domacinstvoDAO.domacinstvo(JIB);
	    AppContext.getInstance().setDomacinstvo(d);
	}
	
	
	public void switchToHomeScene(Event event) throws IOException {
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
	
	public void switchToZaposleniScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneZaposleni.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena zaposleni");
	}
	
	public void switchToKupciScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneKupci.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena kupci");
	}
	
	public void switchToProizvodiScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneProizvodi.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena proizvodi");
	}
	
	public void switchToNarudzbeScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneNarudzbe.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena narudzbe");
	}
	
	public void switchToIsporukeScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneIsporuke.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena isporuke");
	}
	
	public void switchToMlijekoScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneMlijeko.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena mlijeko");
	}
	
	public void switchToPreradaMlijekaScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("ScenePreradaMlijeka.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena preradaMlijeka");
	}
	
	public void switchToZivotinjeScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneZivotinje.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena zivotinje");
	}
	
	public void switchToDomacinstvoInfoScene(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("SceneDomacinstvo.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		stage.setScene(scene);		
		stage.show();

		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});			
		
		System.out.println("Scenes switched - scena domacinstvo info");
	}
	
	@FXML
	private void openTopProizvodiScene(ActionEvent e) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("TopProizvodiScene.fxml"));
	    Parent root = loader.load();

	    Stage stage = new Stage();
	    stage.setTitle("Top proizvodi domaćinstva");
	    stage.setScene(new Scene(root));
	    stage.show();
	    System.out.println("Scenes switched - scena top proizvodi domacinstva");
	}

}