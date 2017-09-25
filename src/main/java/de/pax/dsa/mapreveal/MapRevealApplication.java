package de.pax.dsa.mapreveal;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.MockSessionImpl;
import de.pax.dsa.di.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MapRevealApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		Context context = new Context();
	
		MapRevealContent mapRevealContent = new MapRevealContent();
		Group build = mapRevealContent.build();

		
		
		Scene scene = new Scene(build);
		
		stage.setHeight(500);
		stage.setWidth(500);
		stage.setScene(scene);
		stage.show();
	}
}