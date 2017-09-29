package de.pax.dsa.ui;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.Context;
import de.pax.dsa.ui.internal.GameTable;
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

		context.set(Pane.class, mapPane);
		
		gameTable = context.create(GameTable.class);


		doMovesButton.setOnAction(e -> {
			gameTable.doMoves();
		});

	}

	@FXML
	private void handleLogin() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LoginDialogController.class.getResource("LoginDialog.fxml"));
		AnchorPane page = null;
		try {
			page = loader.load();
		} catch (IOException e) {
			logger.error(e.getMessage());
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
		controller.onLogin(info -> session.connect(info.getUserName(), info.getPassword()));
		
		dialogStage.showAndWait();
	}

	@FXML
	private void handleAddElement() {
		gameTable.addCircle();
	}

}
