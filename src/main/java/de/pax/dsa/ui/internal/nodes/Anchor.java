package de.pax.dsa.ui.internal.nodes;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Anchor extends Circle {
	
	
	public Anchor(String id, DoubleProperty x, DoubleProperty y) {
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
