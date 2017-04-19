package de.pax.dsa.ui.internal;

import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MoveCenterTransition extends Transition {

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
		
		setCycleDuration(Duration.seconds(1));
	}

	@Override
	protected void interpolate(double frac) {
		circle.setCenterX(startX + (xDistance * frac));
		circle.setCenterY(startY + (yDistance * frac));
	}
}
