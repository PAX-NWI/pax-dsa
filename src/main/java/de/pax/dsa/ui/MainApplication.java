package de.pax.dsa.ui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
 
public class MainApplication extends Application{
 
	public static void main(String[] args){
		launch(args);
	}
 
	public void start(Stage stage) throws Exception {
		BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("IcarusMain.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}