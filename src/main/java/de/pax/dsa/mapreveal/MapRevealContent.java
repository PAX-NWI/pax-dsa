package de.pax.dsa.mapreveal;

import java.util.ArrayList;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.mapreveal.moveables.Figure;
import de.pax.dsa.mapreveal.moveables.MoveableGroup;
import de.pax.dsa.mapreveal.moveables.RevealRectangle;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class MapRevealContent {

	@Inject
	private Logger logger;

	final double SCALE_DELTA = 1.1;

	public Group build() {

		ImageNode img = new ImageNode("file:src/main/resources/KibaMapSharped.png", 0, 0);

		int rectSizeX = 4;
		int rectSizeY = 4;

		int numberOfXRects = (int) (img.getBoundsInLocal().getWidth() / rectSizeX) + 1;

		int numberOfYRects = (int) (img.getBoundsInLocal().getHeight() / rectSizeX) + 1;

		ArrayList<Node> nodes = new ArrayList<>();

		for (int x = 0; x < numberOfXRects; x++) {

			for (int y = 0; y < numberOfYRects; y++) {
				RevealRectangle rectangle = new RevealRectangle(rectSizeX, rectSizeY);
				rectangle.setX(x * rectSizeX);
				rectangle.setY(y * rectSizeY);

				nodes.add(rectangle);

			}

		}

		Figure figure = new Figure(15);
		DragEnabler.enableDrag(figure, e -> {
			long startTime = System.currentTimeMillis();
			nodes.parallelStream().forEach(node -> {
				if (node instanceof RevealRectangle) {
					RevealRectangle rectangle = (RevealRectangle) node;

					boolean intersects = intersects(rectangle, figure.getSightMarker());
					if (intersects) {
						rectangle.reveal();
					} else {
						rectangle.unReveal();
					}
				}
			});
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			System.out.println(elapsedTime);
		});

		MoveableGroup group = new MoveableGroup();
		group.getChildren().add(img);
		group.getChildren().addAll(nodes);

		DragEnabler.enableDrag(group, null);

		// Canvas grid = GridFactory.createGrid(50);
		// grid.setStyle("-fx-background-color: cornsilk;");

		MoveableGroup all = new MoveableGroup(group, figure);

		all.setOnScroll(event -> {
			event.consume();
			if (event.getDeltaY() == 0) {
				return;
			}
			double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;
			all.setScaleX(all.getScaleX() * scaleFactor);
			all.setScaleY(all.getScaleY() * scaleFactor);

		});
		return all;
	}

	private boolean intersects(Shape first, Shape second) {
		// fast check if it is close and needs to be checked
		if (!first.getBoundsInParent().intersects(second.getBoundsInParent())) {
			return false;
		}
		// expensive check if it really overlaps
		return (Shape.intersect(first, second).getBoundsInLocal().getWidth() > 1);
	}

}
