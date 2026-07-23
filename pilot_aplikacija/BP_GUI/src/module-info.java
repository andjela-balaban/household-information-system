module BP_GUI {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires BP_PROJEKTNI_ZADATAK;
	
	opens application to javafx.graphics, javafx.fxml;
}
