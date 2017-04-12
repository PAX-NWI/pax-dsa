package de.pax.dsa.ui;

import de.pax.dsa.ui.BoundsPlayground.Delta;
import de.pax.dsa.ui.internal.Anchor;
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

		final Anchor anchor1 = new Anchor("Anchor 1", line.startXProperty(), line.startYProperty());
		final Anchor anchor2 = new Anchor("Anchor 2", line.endXProperty(), line.endYProperty());


		enableDrag(anchor1);
		enableDrag(anchor2);
		Button button = new Button("Do Move");
		
		
		
		
		button.setOnAction(e -> {
		
			anchor1.setCenterX(anchor2.getCenterX());
			anchor1.setCenterY(anchor2.getCenterY());
		});

		final Group group = new Group(line, anchor1, anchor2,button);
		

		// layout the scene.
		final StackPane background = new StackPane();
		background.setStyle("-fx-background-color: cornsilk;");
		final Scene scene = new Scene(new Group(background, group), 600, 500);
		background.prefHeightProperty().bind(scene.heightProperty());
		background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();

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
