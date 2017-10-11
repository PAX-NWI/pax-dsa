package de.pax.dsa.ui.internal.nodes;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ImageNode extends Group implements I2DObject {

	private ImageView imageView;
	private Image image;
	private Circle rotateAnchor;
	private boolean isRotating;

	public ImageNode(String id, String path, double x, double y) {
		this(id, new Image(path), x, y);
	}

	public ImageNode(String id, Image image, double x, double y) {
		this.image = image;
		imageView = new ImageView(image);

		rotateAnchor = new Circle(5, Color.ORANGE);
		
		setId(id);
		setX(x);
		setY(y);

		getChildren().addAll(imageView, rotateAnchor);
	}
	
	public Circle getRotateAnchor() {
		return rotateAnchor;
	}

	public Point2D getCenter() {
		return new Point2D(getX() + getWidth() / 2, getY() + getHeight() / 2);
	}

	public void setImage(Image image) {
		imageView.setImage(image);
	}

	public ImageView getImageView() {
		return imageView;
	}

	public double getWidth() {
		return image.getWidth();
	}

	public double getHeight() {
		return image.getHeight();
	}

	@Override
	public double getX() {
		return imageView.getX();
	}

	@Override
	public void setX(double x) {
		imageView.setX(x);
		rotateAnchor.setCenterX(x + getWidth() / 2);
	}

	@Override
	public double getY() {
		return imageView.getY();
	}

	@Override
	public void setY(double y) {
		imageView.setY(y);
		rotateAnchor.setCenterY(y-10);
	}

	public void setIsRotating(boolean isRotating) {
		this.isRotating = isRotating;
	}

	public boolean isRotating() {
		return isRotating;
	}
	
}
