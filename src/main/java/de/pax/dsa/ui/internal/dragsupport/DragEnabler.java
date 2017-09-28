package de.pax.dsa.ui.internal.dragsupport;

import java.util.function.Consumer;

import de.pax.dsa.model.messages.PositionUpdatedMessage;
import javafx.scene.Cursor;
import javafx.scene.Node;

public class DragEnabler {

	public static void enableDrag(Node node, I2DObject i2dObject, Consumer<PositionUpdatedMessage> onDragComplete) {

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
				onDragComplete.accept(new PositionUpdatedMessage(node.getId(), i2dObject.getX(), i2dObject.getY()));
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
	
	public static void enableDrag(Node node, Consumer<PositionUpdatedMessage> onDragComplete) {
		enableDrag(node, (I2DObject) node, onDragComplete);
	}

}
