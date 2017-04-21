package de.pax.dsa.ui.internal.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MoveCenterTransition extends Transition {

	private static final int SPEED_FACTOR = 5;
	private Circle circle;
	private double yDistance;
	private double xDistance;
	private double startX;
	private double startY;

	public MoveCenterTransition(Circle circle, double toX, double toY) {
		this.circle = circle;
		startX = circle.getCenterX();
		startY = circle.getCenterY();

		yDistance = toY - startY;
		xDistance = toX - startX;

		double dist = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

		setCycleDuration(Duration.millis(dist * SPEED_FACTOR));
		
		setInterpolator(Interpolator.LINEAR);
	}

	@Override
	protected void interpolate(double frac) {
		circle.setCenterX(startX + (xDistance * frac));
		circle.setCenterY(startY + (yDistance * frac));
	}
}
