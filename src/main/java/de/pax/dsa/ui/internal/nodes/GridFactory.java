package de.pax.dsa.ui.internal.nodes;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridFactory {
	
	public static Canvas createGrid(int offset) {

		double w = 4000;// getBoundsInLocal().getWidth();
		double h = 4000;// getBoundsInLocal().getHeight();

		// add grid
		Canvas grid = new Canvas(w, h);

		// don't catch mouse events
		grid.setMouseTransparent(true);

		GraphicsContext gc = grid.getGraphicsContext2D();

		gc.setStroke(Color.LIGHTGRAY);
		gc.setLineWidth(1);

		// draw grid lines
		for (double i = offset; i < w; i += offset) {
			gc.strokeLine(i, 0, i, h);
			gc.strokeLine(0, i, w, i);
		}

		grid.toBack();
		return grid;
	}

}
