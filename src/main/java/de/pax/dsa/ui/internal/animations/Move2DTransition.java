package de.pax.dsa.ui.internal.animations;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Move2DTransition extends Transition {

	private I2DObject node;
	private double yDistance;
	private double xDistance;
	private double startX;
	private double startY;

	public Move2DTransition(I2DObject node, double toX, double toY , int speed) {
		this.node = node;
		startX = node.getX();
		startY = node.getY();

		yDistance = toY - startY;
		xDistance = toX - startX;

		double dist = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

		setCycleDuration(Duration.millis(dist * speed));
		
		setInterpolator(Interpolator.LINEAR);
	}

	@Override
	protected void interpolate(double frac) {
		node.setX(startX + (xDistance * frac));
		node.setY(startY + (yDistance * frac));
	}
}
