package de.pax.dsa.mapreveal.moveables;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.shape.Circle;

public class MoveableCircle extends Circle implements I2DObject {

	@Override
	public double getX() {
		return getCenterX();
	}

	@Override
	public void setX(double x) {
		setCenterX(x);
	}

	@Override
	public double getY() {
		return getCenterY();
	}

	@Override
	public void setY(double y) {
		setCenterY(y);
	}

}
