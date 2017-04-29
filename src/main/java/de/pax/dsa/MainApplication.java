package de.pax.dsa;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.MockSessionImpl;
import de.pax.dsa.di.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
		Pane rootPane = (Pane) fxmlLoader.load(getClass().getResource("ui/RootPane.fxml").openStream());
		context.wire(fxmlLoader.getController());

		Scene scene = new Scene(rootPane);
		stage.setScene(scene);
		stage.show();
	}
}