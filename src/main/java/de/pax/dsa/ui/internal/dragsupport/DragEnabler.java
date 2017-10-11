package de.pax.dsa.ui.internal.dragsupport;

import java.util.function.Consumer;

import de.pax.dsa.model.messages.ElementRotatedMessage;
import de.pax.dsa.model.messages.ElementMovedMessage;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * Provides drag support for nodes that implement I2DObject, returns
 * PositionUpdatedMessage when drag is complete which can be directly forwarded
 * as message to the session
 * 
 * @author alex
 *
 */
public class DragEnabler {

	public static void enableDrag(Node node, Consumer<ElementMovedMessage> onDragComplete) {
		enableDrag(node, (I2DObject) node, onDragComplete);
	}

	private static void enableDrag(Node node, I2DObject i2dObject, Consumer<ElementMovedMessage> onDragComplete) {

		final DragDelta dragDelta = new DragDelta();
		node.setOnMousePressed(mouseEvent -> {
			// record a delta distance for the drag and drop operation.
			dragDelta.setX(i2dObject.getX() - mouseEvent.getSceneX());
			dragDelta.setY(i2dObject.getY() - mouseEvent.getSceneY());
			node.getScene().setCursor(Cursor.MOVE);
		});
		node.setOnMouseReleased(mouseEvent -> {
			if (isRotating(node)) {
				return;
			}
			node.getScene().setCursor(Cursor.HAND);
			if (onDragComplete != null) {
				onDragComplete.accept(new ElementMovedMessage(node.getId(), i2dObject.getX(), i2dObject.getY()));
			}
		});
		node.setOnMouseDragged(mouseEvent -> {
			if (isRotating(node)) {
				return;
			}
			i2dObject.setX(mouseEvent.getSceneX() + dragDelta.getX());
			i2dObject.setY(mouseEvent.getSceneY() + dragDelta.getY());
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

	private static boolean isRotating(Node node) {
		if (node instanceof ImageNode) {
			return ((ImageNode) node).isRotating();
		}
		return false;
	}

	public static void enableRotate(Circle turnAnchor, ImageNode node,
			Consumer<ElementRotatedMessage> onRotateComplete) {
		turnAnchor.setOnMousePressed(mouseEvent -> {
			node.setIsRotating(true);
			turnAnchor.getScene().setCursor(Cursor.H_RESIZE);
		});
		turnAnchor.setOnMouseReleased(mouseEvent -> {
			mouseEvent.consume();
			turnAnchor.getScene().setCursor(Cursor.HAND);
			node.setIsRotating(false);
			if (onRotateComplete != null) {
				onRotateComplete.accept(new ElementRotatedMessage(node.getId(), (int) node.getRotate()));
			}
		});
		turnAnchor.setOnMouseDragged(mouseEvent -> {
			Image image = new Image("rotate.png");
			turnAnchor.getScene().setCursor(new ImageCursor(image));
			node.setRotate(computeRotateBetween(mouseEvent, node));
		});
		turnAnchor.setOnMouseEntered(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				turnAnchor.getScene().setCursor(Cursor.HAND);
			}
		});
		turnAnchor.setOnMouseExited(mouseEvent -> {
			if (!mouseEvent.isPrimaryButtonDown()) {
				turnAnchor.getScene().setCursor(Cursor.DEFAULT);
			}
		});

	}

	/**
	 * http://board.gulli.com/thread/1347561-js-winkel-zwischen-zwei-punkten-berechnen/
	 */
	private static int computeRotateBetween(MouseEvent mouseEvent, ImageNode node) {

		double a = mouseEvent.getSceneX() - node.getCenter().getX();
		double b = mouseEvent.getSceneY() - node.getCenter().getY();

		double atan2 = Math.atan2(a, b);

		double angle = 360 / (2 * Math.PI) * atan2;

		return (int) (angle * -1 + 180);
	}

	/**
	 * Class to store the drag delta values, existing Point2D class can not be
	 * used because it does not have setters which are required in the
	 * DragEnabler implementation due to the final nature of the DragDelta
	 * 
	 * @author alex
	 *
	 */
	public static class DragDelta {

		private double x;
		private double y;

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}
	}

}
