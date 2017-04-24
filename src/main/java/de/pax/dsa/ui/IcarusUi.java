package de.pax.dsa.ui;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.MockSessionImpl;
import de.pax.dsa.di.Context;
import de.pax.dsa.model.PositionUpdate;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import de.pax.dsa.ui.internal.nodes.GridFactory;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.TwoStageMoveNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class IcarusUi extends Application {

	private Group group;

	private Logger logger = LoggerFactory.getLogger(IcarusUi.class);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Context context = new Context();
		IIcarusSession session = context.create(MockSessionImpl.class);
		context.set(IIcarusSession.class, session);

		session.connect("user", "password");

		// Grid grid = new Grid(50);
		TwoStageMoveNode nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		nodeA.setMoveTarget(100, 300);
		TwoStageMoveNode nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		nodeB.setMoveTarget(200, 500);
		ImageNode img = new ImageNode("file:src/main/resources/festum.png", 300, 200);

		Consumer<PositionUpdate> onDragComplete = session::sendPositionUpdate;
		DragEnabler.enableDrag(nodeA, onDragComplete);
		DragEnabler.enableDrag(nodeB, onDragComplete);
		DragEnabler.enableDrag(img, onDragComplete);

		Button move = new Button("Do Moves");
		move.setOnAction(e -> {
			nodeA.commitMove();
			nodeB.commitMove();
		});

		group = new Group(img, nodeA, nodeB, move);

		session.onPositionUpdate(positionUpdate -> {
			I2DObject node = getFromGroup(positionUpdate.getId(), group);
			logger.info("received " + positionUpdate);
			node.setX(positionUpdate.getX());
			node.setY(positionUpdate.getY());
		});

		// layout the scene.
		// final StackPane background = new StackPane();
		// background.setStyle("-fx-background-color: cornsilk;");
		Canvas grid = GridFactory.createGrid(50);
		grid.setStyle("-fx-background-color: cornsilk;");
		Group root = new Group(group, grid);
		final Scene scene = new Scene(root, 1000, 800);

		grid.widthProperty().bind(scene.widthProperty()); // does not really
															// work :(
		grid.heightProperty().bind(scene.heightProperty());

		// background.prefHeightProperty().bind(scene.heightProperty());
		// background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();
	}

	private I2DObject getFromGroup(String id, Group group) {
		List<Node> collect = group.getChildren().stream().filter(e -> id.equals(e.getId()))
				.collect(Collectors.toList());
		if (collect.size() != 1) {
			throw new IllegalStateException("ID: " + id + "exists not exactly once (" + collect.size() + " times)");
		} else {
			return (I2DObject) collect.get(0);
		}
	}

}
