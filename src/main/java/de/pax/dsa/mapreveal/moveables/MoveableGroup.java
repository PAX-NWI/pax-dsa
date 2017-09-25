package de.pax.dsa.mapreveal.moveables;

import java.util.ArrayList;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.Group;
import javafx.scene.Node;

public class MoveableGroup extends Group implements I2DObject{

	public MoveableGroup(Node... nodes) {
		super(nodes); 
	}

	public MoveableGroup(ArrayList<Node> nodes) {
		super(nodes);
	}

	@Override
	public double getX() {
		return getChildren().get(0).getTranslateX();
	}

	@Override
	public void setX(double x) {
		getChildren().stream().forEach(e -> e.setTranslateX(x));
	}

	@Override
	public double getY() {
		return getChildren().get(0).getTranslateY();
	}

	@Override
	public void setY(double y) {
		getChildren().stream().forEach(e -> e.setTranslateY(y));
	}


}
