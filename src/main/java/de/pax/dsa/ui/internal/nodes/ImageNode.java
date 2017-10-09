package de.pax.dsa.ui.internal.nodes;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageNode extends Group implements I2DObject {

	private ImageView imageView;
	private Image image;

	public ImageNode(String id,String path, double x, double y) {
		this(id, new Image(path), x, y);
	}

	public ImageNode(String id, Image image, double x, double y) {
		this.image = image;
		imageView = new ImageView(image);
		imageView.setX(x);
		imageView.setY(y);

		setId(id);
		getChildren().add(imageView);
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
	}

	@Override
	public double getY() {
		return imageView.getY();
	}

	@Override
	public void setY(double y) {
		imageView.setY(y);
	}

}
