package de.pax.dsa.ui.internal.nodes;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageNode extends Group implements I2DObject {

	private ImageView iv;

	public ImageNode(String path, double x, double y) {
		this(path, new Image(path), x, y);
	}

	public ImageNode(String id, Image image, double x, double y) {
		iv = new ImageView(image);
		iv.setX(x);
		iv.setY(y);

		setId(id);
		getChildren().add(iv);
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
