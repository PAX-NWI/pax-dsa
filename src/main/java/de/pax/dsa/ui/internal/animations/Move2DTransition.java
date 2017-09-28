package de.pax.dsa.ui.internal.animations;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.util.Duration;

public class Move2DTransition extends Transition {

	private static final int SPEED_FACTOR = 5;
	private I2DObject circle;
	private double yDistance;
	private double xDistance;
	private double startX;
	private double startY;

	public Move2DTransition(I2DObject i2dObject, double toX, double toY) {
		this.circle = i2dObject;
		startX = i2dObject.getX();
		startY = i2dObject.getY();

		yDistance = toY - startY;
		xDistance = toX - startX;

		double dist = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

		setCycleDuration(Duration.millis(dist * SPEED_FACTOR));
		
		setInterpolator(Interpolator.LINEAR);
	}

	@Override
	protected void interpolate(double frac) {
		circle.setX(startX + (xDistance * frac));
		circle.setY(startY + (yDistance * frac));
	}
}
