package de.pax.dsa.ui.internal;

import java.util.function.Consumer;

import de.pax.dsa.connection.PositionUpdate;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class TwoStageMoveNode extends Group {

	private Anchor position;
	private Anchor moveTarget;
	private Consumer<PositionUpdate> targetChangedConsumer = t -> {
	};

	public TwoStageMoveNode(String string, double x, double y) {
		Line line = new Line(x, y, x, y);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStroke(Color.MIDNIGHTBLUE);
		line.setStrokeWidth(5);

		position = new Anchor("position", line.startXProperty(), line.startYProperty());
		moveTarget = new Anchor("marker", line.endXProperty(), line.endYProperty());
		moveTarget.setFill(Color.ALICEBLUE.deriveColor(1, 1, 1, 0.5));

		enableDrag(moveTarget);

		getChildren().addAll(line, position, moveTarget);

		setId(string);
	}

	public void setMoveTarget(double x, double y) {
		moveTarget.setCenterX(x);
		moveTarget.setCenterY(y);
	}

	public void commitMove() {
		MoveCenterTransition move = new MoveCenterTransition(position, moveTarget.getCenterX(),
				moveTarget.getCenterY());
		move.play();
	}

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
		circle.setOnMouseReleased(mouseEvent -> {
			defineMove(circle);
			circle.getScene().setCursor(Cursor.HAND);
		});
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

	private void defineMove(final Circle circle) {
		targetChangedConsumer.accept(new PositionUpdate(this.getId(), circle.getCenterX(), circle.getCenterY()));
	}

	public void onTargetChanged(Consumer<PositionUpdate> targetChangedConsumer) {
		this.targetChangedConsumer = targetChangedConsumer;
	}

}
