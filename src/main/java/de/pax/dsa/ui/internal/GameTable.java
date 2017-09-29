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
import de.pax.dsa.model.messages.ElementMessageConverter;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.ui.internal.animations.Move2DTransition;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import de.pax.dsa.ui.internal.imagednd.ImageDnDController;
import de.pax.dsa.ui.internal.nodes.GridFactory;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.MoveableCircle;
import de.pax.dsa.ui.internal.nodes.TwoStageMoveNode;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

/**
 * Drag and drop:
 * https://blog.idrsolutions.com/2015/05/how-to-implement-drag-and-drop-function-in-a-javafx-application/
 * 
 * @author alexander.bunkowski
 *
 */
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

		ImageDnDController dnd = new ImageDnDController(respone -> {
			ImageNode img = new ImageNode(getCreateStamp(respone.getName()), respone.getImage(), respone.getX(),
					respone.getY());
			DragEnabler.enableDrag(img, session::sendPositionUpdate);
			elementGroup.getChildren().add(img);
		});

		pane.setOnDragDropped(dnd::mouseDragDropped);
		pane.setOnDragOver(dnd::mouseDragOver);

	}

	public void addCircle() {
		MoveableCircle circle = new MoveableCircle(getCreateStamp("circle"), 50, 50, 20);
		elementGroup.getChildren().add(circle);
		DragEnabler.enableDrag(circle, session::sendPositionUpdate);
		session.sendElementAdded(ElementMessageConverter.createCircleAddedMessage(circle));
	}

	private String getCreateStamp(String id) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long time = timestamp.getTime();
		String separator = "-:-";
		return id + separator + session.getUserName() + separator + Long.toString(time);
	}

	public Group build() {
		Consumer<PositionUpdatedMessage> sendPositionUpdate = session::sendPositionUpdate;

		nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		nodeA.setMoveTarget(100, 300);
		nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		nodeB.setMoveTarget(200, 500);
		ImageNode img = new ImageNode("file:src/main/resources/festum.png", 300, 200);

		DragEnabler.enableDrag(nodeA, sendPositionUpdate);
		DragEnabler.enableDrag(nodeB, sendPositionUpdate);
		DragEnabler.enableDrag(img, sendPositionUpdate);

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
				DragEnabler.enableDrag(newCircle, sendPositionUpdate);
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
