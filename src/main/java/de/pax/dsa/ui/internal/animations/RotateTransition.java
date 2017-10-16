package de.pax.dsa.ui.internal.animations;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class RotateTransition extends Transition {

	private static final int SPEED_FACTOR = 5;
	private Node node;
	private double currentAngle;
	private double rotationToBeDone;

	public RotateTransition(Node node, double targetAngle) {
		this.node = node;
		currentAngle = node.getRotate();
		rotationToBeDone = targetAngle - currentAngle;

		//choose shortest path to turn to
		if (rotationToBeDone > 180) {
			rotationToBeDone -= 360;
		}
		if (rotationToBeDone < -180) {
			rotationToBeDone += 360;
		}

		setCycleDuration(Duration.millis(Math.abs(rotationToBeDone) * SPEED_FACTOR));
		setInterpolator(Interpolator.LINEAR);
	}

	@Override
	protected void interpolate(double frac) {
		node.setRotate(currentAngle + rotationToBeDone * frac);
	}
}
