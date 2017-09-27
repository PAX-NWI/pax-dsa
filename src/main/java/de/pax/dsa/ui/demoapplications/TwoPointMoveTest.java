package de.pax.dsa.ui.demoapplications;

import de.pax.dsa.ui.internal.animations.MoveCenterTransition;
import de.pax.dsa.ui.internal.nodes.AnchorCircle;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class TwoPointMoveTest extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Line line = new Line(150, 150, 150, 150);
		line.setId("Line");
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStroke(Color.MIDNIGHTBLUE);
		line.setStrokeWidth(5);

		final AnchorCircle position = new AnchorCircle("Position", line.startXProperty(), line.startYProperty());
		final AnchorCircle moveTarget = new AnchorCircle("Move Target", line.endXProperty(), line.endYProperty());

		enableDrag(moveTarget);
		Button button = new Button("Do Move");

		button.setOnAction(e -> {
			moveTo(position, moveTarget);
		});

		final Group group = new Group(line, position, moveTarget, button);

		final Group group2 = new Group(group);
		
		
		// layout the scene.
		final StackPane background = new StackPane();
		background.setStyle("-fx-background-color: cornsilk;");
		final Scene scene = new Scene(new Group(background, group2), 600, 500);
		background.prefHeightProperty().bind(scene.heightProperty());
		background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();

	}

	private void moveTo(AnchorCircle anchor1, AnchorCircle anchor2) {
		MoveCenterTransition move = new MoveCenterTransition(anchor1, anchor2.getCenterX(), anchor2.getCenterY());
		move.play();
	}

	// records relative x and y co-ordinates.
	class Delta {
		double x, y;
	}

	// make a node movable by dragging it around with the mouse.
	private void enableDrag(final Circle circle) {
		final Delta dragDelta = new Delta();
		circle.setOnMousePressed(mouseEvent -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.x = circle.getCenterX() - mouseEvent.getX();
			dragDelta.y = circle.getCenterY() - mouseEvent.getY();
			circle.getScene().setCursor(Cursor.MOVE);
		});
		circle.setOnMouseReleased(mouseEvent -> circle.getScene().setCursor(Cursor.HAND));
		circle.setOnMouseDragged(mouseEvent -> {
			circle.setCenterX(mouseEvent.getX() + dragDelta.x);
			circle.setCenterY(mouseEvent.getY() + dragDelta.y);
		});
		circle.setOnMouseEntered(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				circle.getScene().setCursor(Cursor.HAND);
			}
		});
		circle.setOnMouseExited(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				circle.getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

}
