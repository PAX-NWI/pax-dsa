package de.pax.dsa;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.XmppIcarusSession;
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
		IIcarusSession session = context.create(XmppIcarusSession.class);
		session.connect(System.getProperty("user1_username"), System.getProperty("user1_password"));
		
		
		context.set(IIcarusSession.class, session);

		FXMLLoader fxmlLoader = new FXMLLoader();
		Pane rootPane = (Pane) fxmlLoader.load(getClass().getResource("ui/RootPane.fxml").openStream());
		context.wire(fxmlLoader.getController());

		Scene scene = new Scene(rootPane);
		stage.setScene(scene);
		stage.show();
	}
}