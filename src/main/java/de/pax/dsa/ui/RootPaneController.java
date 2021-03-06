package de.pax.dsa.ui;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.Context;
import de.pax.dsa.ui.internal.GameTable;
import de.pax.dsa.ui.internal.GameTableElements;
import de.pax.dsa.ui.internal.contextmenus.NodeContextMenuBuilder;
import de.pax.dsa.ui.internal.contextmenus.PaneContextMenuBuilder;
import de.pax.dsa.ui.logindialog.LoginDialogController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RootPaneController {

	@FXML
	private Pane mapPane;

	@FXML
	private Button doMovesButton;

	@Inject
	private Logger logger;

	@Inject
	private Context context;

	@Inject
	private Stage primaryStage;
	
	@Inject
	private IIcarusSession session;

	private GameTable gameTable;

	@PostConstruct
	private void postConstruct() {

		//setup the context, order is important!
		context.set(Pane.class, mapPane);
		context.createAndSet(GameTableElements.class);
		context.createAndSet(NodeContextMenuBuilder.class);
		context.create(PaneContextMenuBuilder.class);
		gameTable = context.create(GameTable.class);
		
		primaryStage.setTitle("offline");
	}

	@FXML
	private void handleLogin() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LoginDialogController.class.getResource("/fxml/LoginDialog.fxml"));
		AnchorPane page = null;
		try {
			page = loader.load();
		} catch (IOException e) {
			logger.error("Could not load LoginDialog.fxml", e);
		}
		// Create the dialog Stage.
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Login");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(page);
		dialogStage.setScene(scene);
		
		LoginDialogController controller = loader.getController();
		controller.setStage(dialogStage);
		controller.onLogin(info -> {
			session.connect(info.getUserName(), info.getPassword());
			primaryStage.setTitle(session.getUserName() +"@"+ session.getServer());
		});
		
		dialogStage.showAndWait();
		
	}
	
	@FXML
	private void login1(){
		String user1 = System.getProperty("user1_username");
		String password1 = System.getProperty("user1_password");
		session.connect(user1, password1);
		primaryStage.setTitle(session.getUserName() +"@"+ session.getServer());
	}
	
	@FXML
	private void login2(){
		String user2 = System.getProperty("user2_username");
		String password2 = System.getProperty("user2_password");
		session.connect(user2, password2);
		primaryStage.setTitle(session.getUserName() +"@"+ session.getServer());
	}

	@FXML
	private void handleAddElement() {
		gameTable.addCircle();
	}

}
