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

		TwoStageMoveNode nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		TwoStageMoveNode nodeB = new TwoStageMoveNode("nodeB", 200, 100);

		nodeA.setMoveTarget(100, 300);
		nodeB.setMoveTarget(200, 500);

		Button move = new Button("Do Moves");
		move.setOnAction(e -> {
			nodeA.commitMove();
			nodeB.commitMove();
		});

		final Group group = new Group(nodeA, nodeB, move);

		// layout the scene.
		final StackPane background = new StackPane();
		background.setStyle("-fx-background-color: cornsilk;");
		final Scene scene = new Scene(new Group(background, group), 600, 700);
		background.prefHeightProperty().bind(scene.heightProperty());
		background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();

	}

}
