package de.pax.dsa;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.MockSessionImpl;
import de.pax.dsa.di.Context;
import de.pax.dsa.xmpp.XmppIcarusSession;
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
		
		context.set(Stage.class, stage);
		
		IIcarusSession session = context.create(XmppIcarusSession.class);
		session.connect(System.getProperty("user2_username"), System.getProperty("user2_password"));
		
		context.set(IIcarusSession.class, session);

		FXMLLoader fxmlLoader = new FXMLLoader();
		Pane rootPane = (Pane) fxmlLoader.load(getClass().getResource("ui/RootPane.fxml").openStream());
		context.wire(fxmlLoader.getController());

		Scene scene = new Scene(rootPane);
		stage.setScene(scene);
		stage.show();
	}
}