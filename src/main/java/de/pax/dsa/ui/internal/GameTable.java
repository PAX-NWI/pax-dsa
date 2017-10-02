package de.pax.dsa.ui.internal;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;
import de.pax.dsa.ui.internal.animations.Move2DTransition;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import de.pax.dsa.ui.internal.dragsupport.IdBuilder;
import de.pax.dsa.ui.internal.imagednd.ImageDnDController;
import de.pax.dsa.ui.internal.nodes.GridFactory;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.MoveableCircle;
import de.pax.dsa.ui.internal.nodes.TwoStageMoveNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Drag and drop:
 * https://blog.idrsolutions.com/2015/05/how-to-implement-drag-and-drop-function-in-a-javafx-application/
 * 
 * @author alexander.bunkowski
 *
 */
public class GameTable {

	private static final String IMAGE_FOLDER = "file:images/";
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

		ImageDnDController dnd = new ImageDnDController(response -> {
			String identifier = IdBuilder.build(response.getName(), session.getUserName());
			double x = response.getX();
			double y = response.getY();
			ImageNode img = new ImageNode(identifier, response.getImage(), x, y);
			DragEnabler.enableDrag(img, session::sendPositionUpdate);
			elementGroup.getChildren().add(img);
			session.sendElementAdded(
					new ElementAddedMessage(identifier, ElementType.IMAGE, x, y, img.getWidth(), img.getHeight()));
		});

		pane.setOnDragDropped(dnd::mouseDragDropped);
		pane.setOnDragOver(dnd::mouseDragOver);

	}

	public void addCircle() {
		MoveableCircle circle = new MoveableCircle(IdBuilder.build("circle", session.getUserName()), 50, 50, 10);
		elementGroup.getChildren().add(circle);
		DragEnabler.enableDrag(circle, session::sendPositionUpdate);
		session.sendElementAdded(new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(),
				circle.getY(), circle.getRadius(), circle.getRadius()));
	}

	public Group build() {
		Consumer<PositionUpdatedMessage> sendPositionUpdate = session::sendPositionUpdate;

		// nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		// nodeA.setMoveTarget(100, 300);
		// nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		// nodeB.setMoveTarget(200, 500);
		// ImageNode img = new
		// ImageNode("festumimage","file:src/main/resources/festum.png", 300,
		// 200);
		//
		// DragEnabler.enableDrag(nodeA, sendPositionUpdate);
		// DragEnabler.enableDrag(nodeB, sendPositionUpdate);
		// DragEnabler.enableDrag(img, sendPositionUpdate);
		//
		// elementGroup = new Group(img, nodeA, nodeB);

		elementGroup = new Group();

		session.onPositionUpdate(positionUpdate -> {
			I2DObject node = getFromGroup(positionUpdate.getId(), elementGroup);
			logger.info("received " + positionUpdate);

			new Move2DTransition(node, positionUpdate.getX(), positionUpdate.getY(), 2).play();
		});

		session.onElementAdded(message -> {
			ElementType elementType = message.getElementType();
			if (elementType == ElementType.CIRCLE) {
				MoveableCircle newCircle = new MoveableCircle(message.getId(), message.getX(), message.getY(),
						message.getW());
				elementGroup.getChildren().add(newCircle);
				DragEnabler.enableDrag(newCircle, sendPositionUpdate);
			} else if (elementType == ElementType.IMAGE) {
				String id = message.getId();
				String name = IdBuilder.getName(id);
				ImageNode imageNode = null;
				if (!new File(IMAGE_FOLDER + name).exists()) {
					String owner = IdBuilder.getOwner(id);
					session.sendRequestFile(new RequestFileMessage(owner, name, session.getUserName()));
					imageNode = new ImageNode(id, IMAGE_FOLDER + "waiting.gif", message.getX(), message.getY());
				} else {
					imageNode = new ImageNode(id, IMAGE_FOLDER + name, message.getX(), message.getY());
				}
				elementGroup.getChildren().add(imageNode);
				DragEnabler.enableDrag(imageNode, sendPositionUpdate);
			} else {
				logger.warn("Unknown element type {}", elementType);
			}
		});

		session.onRequestFile(new Consumer<RequestFileMessage>() {

			@Override
			public void accept(RequestFileMessage t) {
				// if the owner of the file is me i have to send it
				if (t.getOwner().equals(session.getUserName())) {
					session.sendFile(t.getRequester(), new File("images/"+t.getFileName()));
				}

			}
		});

		session.onFileReceived(f -> {
			//TODO only replace those that dont have the correct image yet
			ImageNode node = (ImageNode) getFromGroupByFilename(f.getName(), elementGroup);
			node.setImage(new Image("images/" + f.getName()));
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

	private I2DObject getFromGroupByFilename(String filename, Group group) {
		List<Node> collect = group.getChildren().stream().filter(e -> {
			String name = IdBuilder.getName(e.getId());
			return filename.equals(name);
		}).collect(Collectors.toList());
		if (collect.size() != 1) {
			throw new IllegalStateException(
					"ID: " + filename + "exists not exactly once (" + collect.size() + " times)");
		} else {
			return (I2DObject) collect.get(0);
		}
	}

}
