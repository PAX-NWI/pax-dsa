package de.pax.dsa.ui.internal.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Move2DTransition extends Transition {

	private Node node;
	private double yDistance;
	private double xDistance;
	private double startX;
	private double startY;

	public Move2DTransition(Node node, double toX, double toY , int speed) {
		this.node = node;
		startX = node.getLayoutX();
		startY = node.getLayoutY();

		yDistance = toY - startY;
		xDistance = toX - startX;

		double dist = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

		setCycleDuration(Duration.millis(dist * speed));
		
		setInterpolator(Interpolator.LINEAR);
	}

	@Override
	protected void interpolate(double frac) {
		node.setLayoutX(startX + (xDistance * frac));
		node.setLayoutY(startY + (yDistance * frac));
	}
}
