package de.pax.dsa.ui;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.MockSessionImpl;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.TwoStageMoveNode;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IcarusUi extends Application {

	private Group group;

	private Logger logger = LoggerFactory.getLogger(IcarusUi.class);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		IIcarusSession session = new MockSessionImpl();

		session.connect("user", "password");

		TwoStageMoveNode nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		nodeA.setMoveTarget(100, 300);
		TwoStageMoveNode nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		nodeB.setMoveTarget(200, 500);
		ImageNode img = new ImageNode("file:src/main/resources/festum.png",300,200);
	
 
		DragEnabler.enableDrag(nodeA, positionUpdate -> {
			session.sendPositionUpdate(positionUpdate);
		});
		DragEnabler.enableDrag(nodeB, positionUpdate -> {
			session.sendPositionUpdate(positionUpdate);
		});
		DragEnabler.enableDrag(img, positionUpdate -> {
			session.sendPositionUpdate(positionUpdate);
		});


		Button move = new Button("Do Moves");
		move.setOnAction(e -> {
			nodeA.commitMove();
			nodeB.commitMove();
		});

		group = new Group(nodeA, nodeB, move, img);

		session.onPositionUpdate(positionUpdate -> {
			Node node = getFromGroup(positionUpdate.getId(), group);
			logger.info("received " + positionUpdate +" for " + node.getId());
		//	node.setMoveTarget(positionUpdate.getX(), positionUpdate.getY());
		});

		// layout the scene.
		final StackPane background = new StackPane();
		background.setStyle("-fx-background-color: cornsilk;");
		final Scene scene = new Scene(new Group(background, group), 1000, 800);
		background.prefHeightProperty().bind(scene.heightProperty());
		background.prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();
	}

	private Node getFromGroup(String id, Group group) {
		List<Node> collect = group.getChildren().stream().filter(e -> id.equals(e.getId()))
				.collect(Collectors.toList());
		if (collect.size() != 1) {
			throw new IllegalStateException("ID: " + id + "exists not exactly once (" + collect.size() + " times)");
		} else {
			return collect.get(0);
		}
	}

}
