package de.pax.dsa.mapreveal.moveables;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Figure extends Group implements I2DObject{

	
	private Circle position;
	private Circle sightMarker;

	public Figure(double sightRadius) {
		position = new Circle(2);
		position.setFill(Color.BLACK);
		getChildren().add(position);
		
		sightMarker = new Circle(sightRadius);
		sightMarker.setFill(Color.TRANSPARENT);
		sightMarker.setStrokeWidth(3);
		sightMarker.setStroke(Color.BLACK);
		getChildren().add(sightMarker);
	}

	public Circle getSightMarker() {
		return sightMarker;
	}
	
	@Override
	public double getX() {
		return position.getTranslateX();
	}

	@Override
	public void setX(double x) {
		position.setTranslateX(x);
		sightMarker.setTranslateX(x);
	}

	@Override
	public double getY() {
		return position.getTranslateY();
	}

	@Override
	public void setY(double y) {
		position.setTranslateY(y);
		sightMarker.setTranslateY(y);
	}

}
