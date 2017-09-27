package de.pax.dsa.ui.internal.nodes;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 * Extension of the Circle class to enable updating the center position with
 * bounded properties (from other objects).
 * 
 * @author alexander.bunkowski
 *
 */
public class AnchorCircle extends Circle {

	public AnchorCircle(String id, DoubleProperty x, DoubleProperty y) {
		super(x.get(), y.get(), 20);
		setId(id);
		setFill(Color.CORNFLOWERBLUE.deriveColor(1, 1, 1, 0.5));
		setStroke(Color.CORNFLOWERBLUE);
		setStrokeWidth(2);
		setStrokeType(StrokeType.OUTSIDE);

		x.bind(centerXProperty());
		y.bind(centerYProperty());
	}
}
