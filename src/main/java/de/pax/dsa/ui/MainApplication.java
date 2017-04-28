package de.pax.dsa.ui;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.MockSessionImpl;
import de.pax.dsa.di.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) throws Exception {

		Context context = new Context();
		IIcarusSession session = context.create(MockSessionImpl.class);
		context.set(IIcarusSession.class, session);

		FXMLLoader fxmlLoader = new FXMLLoader();

		BorderPane root = (BorderPane) fxmlLoader.load(getClass().getResource("IcarusMain.fxml").openStream());
		context.wire(fxmlLoader.getController());

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}