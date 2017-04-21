package de.pax.dsa.ui.internal.nodes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Grid extends StackPane {

	public Grid(double offset,GraphicsContext gc) {

		double w = getBoundsInLocal().getWidth();
		double h = getBoundsInLocal().getHeight();

		// don't catch mouse events
		this.setMouseTransparent(true);

	//	GraphicsContext gc = this.getGraphicsContext2D();

		gc.setStroke(Color.GRAY);
		gc.setLineWidth(1);

		// draw grid lines
		for (double i = offset; i < w; i += offset) {
			gc.strokeLine(i, 0, i, h);
			gc.strokeLine(0, i, w, i);
		}

		this.toBack();
	}

}
