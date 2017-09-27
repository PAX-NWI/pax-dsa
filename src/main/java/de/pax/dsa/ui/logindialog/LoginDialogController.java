package de.pax.dsa.ui.logindialog;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginDialogController {

	@FXML
	private TextField userNameField;
	
	@FXML
	private TextField passwordField;

	private Consumer<LoginInformation> loginConsumer;

	private Stage dialogStage;

	public void onLogin(Consumer<LoginInformation> loginConsumer){
		this.loginConsumer = loginConsumer;
	}
	
	@FXML
	private void handleLogin() {
		loginConsumer.accept(new LoginInformation(userNameField.getText(), passwordField.getText()));
		dialogStage.close();
	}

	public void setStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
