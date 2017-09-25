package de.pax.dsa.mapreveal.moveables;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RevealRectangle extends Rectangle implements I2DObject {

	public RevealRectangle(int i, int j) {
		super(i, j);

		setStroke(Color.DARKGRAY);
		setFill(Color.GREY);

		setOpacity(1);
		setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				if (getOpacity() > 0) {
					setOpacity(0);
				} else {
					setOpacity(1);
				}
				
			}
		});

	}

}
