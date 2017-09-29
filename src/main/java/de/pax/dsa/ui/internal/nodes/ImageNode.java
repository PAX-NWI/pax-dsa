package de.pax.dsa.ui.internal.nodes;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageNode extends Group implements I2DObject {

	private ImageView iv;
	private Image image;

	public ImageNode(String id,String path, double x, double y) {
		this(id, new Image(path), x, y);
	}

	public ImageNode(String id, Image image, double x, double y) {
		this.image = image;
		iv = new ImageView(image);
		iv.setX(x);
		iv.setY(y);

		setId(id);
		getChildren().add(iv);
	}

	public double getWidth() {
		return image.getWidth();
	}
	
	public double getHeight() {
		return image.getHeight();
	}
	
	@Override
	public double getX() {
		return iv.getX();
	}

	@Override
	public void setX(double x) {
		iv.setX(x);
	}

	@Override
	public double getY() {
		return iv.getY();
	}

	@Override
	public void setY(double y) {
		iv.setY(y);
	}

}
