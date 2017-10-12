package de.pax.dsa.ui.internal;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.Context;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRotatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;
import de.pax.dsa.ui.internal.animations.Move2DTransition;
import de.pax.dsa.ui.internal.animations.RotateTransition;
import de.pax.dsa.ui.internal.contextmenus.NodeContextMenuBuilder;
import de.pax.dsa.ui.internal.contextmenus.PaneContextMenuBuilder;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import de.pax.dsa.ui.internal.dragsupport.IdBuilder;
import de.pax.dsa.ui.internal.imagednd.ImageDnDController;
import de.pax.dsa.ui.internal.nodes.GridFactory;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import de.pax.dsa.ui.internal.nodes.MoveableCircle;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**
 * Drag and drop:
 * https://blog.idrsolutions.com/2015/05/how-to-implement-drag-and-drop-function-in-a-javafx-application/
 * 
 * Resizing an rectangle woth knobs at the edges:
 * https://stackoverflow.com/questions/26298873/resizable-and-movable-rectangle
 * 
 * @author alexander.bunkowski
 *
 */
public class GameTable {

	private static final String FILE = "file:";
	private static final String IMAGE_FOLDER = "images/";

	@Inject
	private IIcarusSession session;

	@Inject
	private Pane pane;

	@Inject
	private Logger logger;

	@Inject
	private Context context;

	// private TwoStageMoveNode nodeA;
	//
	// private TwoStageMoveNode nodeB;

	private GameTableElements gameTableElements;
	private NodeContextMenuBuilder nodeContextMenuBuilder;

	@PostConstruct
	private void postConstruct() {

		gameTableElements = context.createAndSet(GameTableElements.class);
		nodeContextMenuBuilder = context.createAndSet(NodeContextMenuBuilder.class);
		context.create(PaneContextMenuBuilder.class);

		Canvas grid = GridFactory.createGrid(50);
		grid.setStyle("-fx-background-color: cornsilk;");
		pane.getChildren().add(grid);

		ImageDnDController dnd = new ImageDnDController(response -> {
			String identifier = IdBuilder.build(response.getName(), session.getUserName());
			double x = response.getX();
			double y = response.getY();
			Image image = response.getImage();
			ImageNode imageNode = new ImageNode(identifier, image, x, y);
			DragEnabler.enableDrag(imageNode, session::sendMessage);
			DragEnabler.enableRotate(imageNode.getRotateAnchor(), imageNode, session::sendMessage);
			gameTableElements.add(imageNode);
			nodeContextMenuBuilder.buildContextMenuFor(imageNode);
			session.sendMessage(new ElementAddedMessage(identifier, ElementType.IMAGE, x, y, imageNode.getWidth(),
					imageNode.getHeight()));
		});

		pane.setOnDragDropped(dnd::mouseDragDropped);
		pane.setOnDragOver(dnd::mouseDragOver);

		registerToSessionEvents();

	}

	public void addCircle() {
		MoveableCircle circle = new MoveableCircle(IdBuilder.build("circle", session.getUserName()), 50, 50, 10);
		gameTableElements.add(circle);
		DragEnabler.enableDrag(circle, session::sendMessage);

		nodeContextMenuBuilder.buildContextMenuFor(circle);

		session.sendMessage(new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(), circle.getY(),
				circle.getRadius(), circle.getRadius()));
	}

	private void registerToSessionEvents() {

		// nodeA = new TwoStageMoveNode("nodeA", 100, 100);
		// nodeA.setMoveTarget(100, 300);
		// nodeB = new TwoStageMoveNode("nodeB", 200, 100);
		// nodeB.setMoveTarget(200, 500);
		//
		// DragEnabler.enableDrag(nodeA, sendPositionUpdate);
		// DragEnabler.enableDrag(nodeB, sendPositionUpdate);
		//
		// elementGroup = new Group(img, nodeA, nodeB);

		session.onPositionUpdate(positionUpdate -> {
			Node node = gameTableElements.getById(positionUpdate.getId());
			if (node != null) {
				new Move2DTransition((I2DObject) node, positionUpdate.getX(), positionUpdate.getY(), 2).play();
			}
		});

		session.onElementAdded(message -> {
			ElementType elementType = message.getElementType();
			if (elementType == ElementType.CIRCLE) {
				MoveableCircle newCircle = new MoveableCircle(message.getId(), message.getX(), message.getY(),
						message.getW());
				gameTableElements.add(newCircle);
				DragEnabler.enableDrag(newCircle, session::sendMessage);
				nodeContextMenuBuilder.buildContextMenuFor(newCircle);
			} else if (elementType == ElementType.IMAGE) {
				String id = message.getId();
				String name = IdBuilder.getName(id);
				ImageNode imageNode = null;
				if (!new File(IMAGE_FOLDER + name).exists()) {
					String owner = IdBuilder.getOwner(id);
					session.sendMessage(new RequestFileMessage(owner, name));
					// TODO display a rect with the size of the image to be
					// received and place the waiting gif in the middle of it
					imageNode = new ImageNode(id, "waiting.gif", message.getX(), message.getY());
				} else {
					imageNode = new ImageNode(id, FILE + IMAGE_FOLDER + name, message.getX(), message.getY());
				}
				gameTableElements.add(imageNode);
				DragEnabler.enableDrag(imageNode, session::sendMessage);
				DragEnabler.enableRotate(imageNode.getRotateAnchor(), imageNode, session::sendMessage);
				nodeContextMenuBuilder.buildContextMenuFor(imageNode);
			} else {
				logger.warn("Unknown element type {}", elementType);
			}
		});

		session.onElementRemoved(message -> {
			gameTableElements.remove(gameTableElements.getById(message.getId()));
		});

		session.onElementToTop(message -> {
			gameTableElements.getById(message.getId()).toFront();
		});

		session.onElementToBack(message -> {
			gameTableElements.getById(message.getId()).toBack();
		});

		session.onElementRotated(message -> {
			new RotateTransition(gameTableElements.getById(message.getId()), message.getAngle()).play();
		});

		session.onRequestFile(message -> {
			// if the owner of the file is me i have to send it
			if (message.getOwner().equals(session.getUserName())) {
				session.sendFile(message.getSender(), new File(IMAGE_FOLDER + message.getFileName()));
			}
		});

		session.onFileReceived(file -> {
			List<Node> byFilename = gameTableElements.getByFilename(file.getName());
			for (Node node : byFilename) {
				((ImageNode) node).setImage(new Image(FILE + IMAGE_FOLDER + file.getName()));
			}
		});

		session.onUserEntered(name -> {
			List<Node> my = gameTableElements.getOwnedBy(session.getUserName());
			for (Node node : my) {
				if (node instanceof Circle) {
					MoveableCircle circle = (MoveableCircle) node;
					session.sendMessageToUser(new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(),
							circle.getY(), circle.getRadius(), circle.getRadius()), name);
				} else if (node instanceof ImageNode) {
					ImageNode imageNode = (ImageNode) node;
					session.sendMessageToUser(new ElementAddedMessage(imageNode.getId(), ElementType.IMAGE,
							imageNode.getX(), imageNode.getY(), imageNode.getWidth(), imageNode.getHeight()), name);
				}
				sleep(500);

				if (node.getRotate() != 0) {
					session.sendMessageToUser(new ElementRotatedMessage(node.getId(), (int) node.getRotate()), name);
					sleep(500);
				}

			}
		});

	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			logger.error("Error performing thread sleep", e);
		}
	}

	// public void doMoves() {
	// nodeA.commitMove();
	// nodeB.commitMove();
	// }

}
