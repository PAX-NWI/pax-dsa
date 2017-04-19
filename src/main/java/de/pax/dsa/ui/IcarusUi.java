package de.pax.dsa.ui;

import de.pax.dsa.ui.internal.TwoStageMoveNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IcarusUi extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		TwoStageMoveNode twoStageMoveNode = new TwoStageMoveNode(150,150);
	
		Button button = new Button("Do Move");

		button.setOnAction(e -> {
			twoStageMoveNode.commitMove();
		});

		final Group group = new Group(twoStageMoveNode,button);

		// layout the scene.
		final StackPane background = new StackPane();
		background.setStyle("-fx-background-color: cornsilk;");
		final Scene scene = new Scene(new Group(background, group), 600, 500);
		background.prefHeightProperty().bind(scene.heightProperty());
		background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();

	}	

}
