package de.pax.dsa.ui.internal.nodes;

import de.pax.dsa.ui.internal.animations.MoveCenterTransition;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class TwoStageMoveNode extends Group implements I2DObject {

	private Anchor position;
	private Anchor moveTarget;


	public TwoStageMoveNode(String string, double x, double y) {
		Line line = new Line(x, y, x, y);
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStroke(Color.CORNFLOWERBLUE);
		line.setStrokeWidth(5);
		line.setMouseTransparent(true);

		position = new Anchor("position", line.startXProperty(), line.startYProperty());
		moveTarget = new Anchor("marker", line.endXProperty(), line.endYProperty());
		moveTarget.setFill(Color.TRANSPARENT);

		moveTarget.getStrokeDashArray().addAll(2d, 20d);

		position.setMouseTransparent(true);

		

		getChildren().addAll(line, position, moveTarget);

		setId(string);
	}

	public void setMoveTarget(double x, double y) {
		MoveCenterTransition move = new MoveCenterTransition(moveTarget, x, y);
		move.play();
	}

	public void commitMove() {
		MoveCenterTransition move = new MoveCenterTransition(position, moveTarget.getCenterX(),
				moveTarget.getCenterY());
		move.play();
	}


	@Override
	public double getX() {
		return moveTarget.getCenterX();
	}

	@Override
	public void setX(double x) {
		moveTarget.setCenterX(x);
	}

	@Override
	public double getY() {
		return moveTarget.getCenterY();
	}

	@Override
	public void setY(double y) {
		moveTarget.setCenterY(y);
		
	}

}
