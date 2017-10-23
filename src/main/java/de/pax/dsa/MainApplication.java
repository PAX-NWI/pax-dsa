package de.pax.dsa;

import java.io.File;
import java.io.IOException;

import de.pax.dsa.chatpane.ChatPaneController;
import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.Context;
import de.pax.dsa.di.IUiSynchronize;
import de.pax.dsa.xmpp.XmppIcarusSession;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApplication extends Application {

	private IIcarusSession session;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		File dir = new File("images");
		if (!dir.exists()) {
			dir.mkdir();
		}

		Context context = new Context();

		context.set(IUiSynchronize.class, Platform::runLater);
		context.set(Stage.class, primaryStage);

		session = context.create(XmppIcarusSession.class);
		context.set(IIcarusSession.class, session);

		FXMLLoader fxmlLoader = new FXMLLoader();
		Pane rootPane = fxmlLoader.load(getClass().getResource("/fxml/RootPane.fxml").openStream());
		context.wire(fxmlLoader.getController());

		Scene scene = new Scene(rootPane);
		primaryStage.setScene(scene);
		primaryStage.show();

		createChatStage(primaryStage, context);
	}

	private void createChatStage(Stage primaryStage, Context context) throws Exception {
		final Stage chatStage = new Stage();
		chatStage.initOwner(primaryStage);
		chatStage.setTitle("Chat");
		chatStage.initStyle(StageStyle.UTILITY);
		chatStage.setX(primaryStage.getX() + primaryStage.getWidth());
		chatStage.setY(primaryStage.getY());

		FXMLLoader fxmlLoader = new FXMLLoader();
		Pane chatPane = fxmlLoader.load(ChatPaneController.class.getResource("ChatPane.fxml").openStream());
		context.wire(fxmlLoader.getController());

		Scene scene = new Scene(chatPane);
		chatStage.setScene(scene);
		chatStage.show();

		// ensure the utility window closes when the main app window closes.
		primaryStage.setOnCloseRequest(windowEvent -> chatStage.close());

	}

	@Override
	public void stop() throws Exception {
		session.disconnect();
	}
}