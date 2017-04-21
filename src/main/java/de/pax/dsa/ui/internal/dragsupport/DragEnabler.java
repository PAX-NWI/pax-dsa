package de.pax.dsa.ui.internal.dragsupport;

import java.util.function.Consumer;

import de.pax.dsa.model.PositionUpdate;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class DragEnabler {

	public static void enableDrag(Node node, I2DObject i2dObject, Consumer<PositionUpdate> onDragComplete) {
		
		final Position dragDelta = new Position();
		node.setOnMousePressed(mouseEvent -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.setX(i2dObject.getX() - mouseEvent.getX());
			dragDelta.setY(i2dObject.getY() - mouseEvent.getY());
			node.getScene().setCursor(Cursor.MOVE);
		});
		node.setOnMouseReleased(mouseEvent -> {
			node.getScene().setCursor(Cursor.HAND);
			if (onDragComplete != null) {
				onDragComplete.accept(new PositionUpdate(node.getId(), i2dObject.getX(), i2dObject.getY()));
			}
		});
		node.setOnMouseDragged(mouseEvent -> {
			i2dObject.setX(mouseEvent.getX() + dragDelta.getX());
			i2dObject.setY(mouseEvent.getY() + dragDelta.getY());
		});
		node.setOnMouseEntered(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				node.getScene().setCursor(Cursor.HAND);
			}
		});
		node.setOnMouseExited(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				node.getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

	public static void enableDrag(ImageView imageView, Consumer<PositionUpdate> onDragComplete) {
		I2DObject i2dObject = new I2DObject() {

			@Override
			public double getX() {
				return imageView.getX();
			}

			@Override
			public double getY() {
				return imageView.getY();
			}

			@Override
			public void setY(double y) {
				imageView.setY(y);
			}

			@Override
			public void setX(double x) {
				imageView.setX(x);
			}

		};
		enableDrag(imageView, i2dObject, onDragComplete);

	}

	public static void enableDrag(Circle cirlce, Consumer<PositionUpdate> onDragComplete) {
		I2DObject i2dObject = new I2DObject() {

			@Override
			public double getX() {
				return cirlce.getCenterX();
			}

			@Override
			public double getY() {
				return cirlce.getCenterY();
			}

			@Override
			public void setY(double y) {
				cirlce.setCenterY(y);
			}

			@Override
			public void setX(double x) {
				cirlce.setCenterX(x);
			}

		};
		enableDrag(cirlce, i2dObject, onDragComplete);

	}

}
