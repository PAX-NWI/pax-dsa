package de.pax.dsa.ui.internal;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementMessageConverter;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.ui.internal.animations.Move2DTransition;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import de.pax.dsa.ui.internal.nodes.GridFactory;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.MoveableCircle;
import de.pax.dsa.ui.internal.nodes.TwoStageMoveNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class GameTable {

	@Inject
	private IIcarusSession session;

	@Inject
	private Pane pane;

	@Inject
	private Logger logger;

	private TwoStageMoveNode nodeA;

	private TwoStageMoveNode nodeB;

	private Group elementGroup;

	@PostConstruct
	private void posConstruct() {

		Canvas grid = GridFactory.createGrid(50);
		grid.setStyle("-fx-background-color: cornsilk;");
		pane.getChildren().add(grid);

		pane.getChildren().add(build());
	}

	public void addCircle(String id) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		MoveableCircle circle = new MoveableCircle("" + timestamp.getTime(), 50, 50, 20);
		DragEnabler.enableDrag(circle, session::sendPositionUpdate);
		elementGroup.getChildren().add(circle);
		session.sendElementAdded(ElementMessageConverter.createCircleAddedMessage(circle));
	}

	public Group build() {
		Consumer<PositionUpdatedMessage> onDragComplete = session::sendPositionUpdate;

		nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		nodeA.setMoveTarget(100, 300);
		nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		nodeB.setMoveTarget(200, 500);
		ImageNode img = new ImageNode("file:src/main/resources/festum.png", 300, 200);

		DragEnabler.enableDrag(nodeA, onDragComplete);
		DragEnabler.enableDrag(nodeB, onDragComplete);
		DragEnabler.enableDrag(img, onDragComplete);

		elementGroup = new Group(img, nodeA, nodeB);

		session.onPositionUpdate(positionUpdate -> {
			I2DObject node = getFromGroup(positionUpdate.getId(), elementGroup);
			logger.info("received " + positionUpdate);

			new Move2DTransition(node, positionUpdate.getX(), positionUpdate.getY()).play();
		});

		session.onElementAdded(elementAddedMessage -> {
			ElementType elementType = elementAddedMessage.getElementType();
			if (elementType == ElementType.CIRCLE) {

				MoveableCircle newCircle = ElementMessageConverter.circleFromMessage(elementAddedMessage);
				elementGroup.getChildren().add(newCircle);

			} else {
				logger.warn("Unknown element type {}", elementType);
			}

		});

		return elementGroup;
	}

	public void doMoves() {
		nodeA.commitMove();
		nodeB.commitMove();
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
