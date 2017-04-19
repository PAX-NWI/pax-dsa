package de.pax.dsa.ui.internal;

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

		double dist = (Math.abs(xDistance) + Math.abs(yDistance))/2;

		setCycleDuration(Duration.millis(dist * SPEED_FACTOR));
	}

	@Override
	protected void interpolate(double frac) {
		circle.setCenterX(startX + (xDistance * frac));
		circle.setCenterY(startY + (yDistance * frac));
	}
}
