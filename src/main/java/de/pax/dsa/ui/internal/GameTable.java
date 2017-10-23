package de.pax.dsa.ui.internal;

import java.io.File;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementMovedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementRotatedMessage;
import de.pax.dsa.model.messages.ElementToBackMessage;
import de.pax.dsa.model.messages.ElementToTopMessage;
import de.pax.dsa.model.messages.RequestFileMessage;
import de.pax.dsa.model.sessionEvents.FileReceivedEvent;
import de.pax.dsa.model.sessionEvents.UserJoinedEvent;
import de.pax.dsa.ui.internal.animations.Move2DTransition;
import de.pax.dsa.ui.internal.animations.RotateTransition;
import de.pax.dsa.ui.internal.contextmenus.NodeContextMenuBuilder;
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
	private GameTableElements gameTableElements;
	
	@Inject
	private NodeContextMenuBuilder nodeContextMenuBuilder;

	@PostConstruct
	private void postConstruct() {


		Canvas grid = GridFactory.createGrid(50);
		grid.setStyle("-fx-background-color: cornsilk;");
		pane.getChildren().add(grid);
		grid.toBack();

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

		session.onMessageReceived(ElementMovedMessage.class, message -> {
			Node node = gameTableElements.getById(message.getId());
			if (node != null) {
				new Move2DTransition((I2DObject) node, message.getX(), message.getY(), 2).play();
			}
		});

		session.onMessageReceived(ElementAddedMessage.class, message -> {
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
					// TODO
					// if a user that added images to the gametable already left
					// and a new user enters this doesn't work
					//
					// but when a user adds a new image to the table he is the
					// only one that can send it
					String owner = IdBuilder.getOwner(id);
					// old way: ask all
					 session.sendMessage(new RequestFileMessage(owner, name));

					// new way: ask user directly, make sure he exists
//					boolean fileRequestSent = session.sendMessageToUser(new RequestFileMessage(owner, name), owner);
//
//					if (!fileRequestSent) {
//						List<String> allOtherUsers = session.getAllOtherUsers();
//						Collections.shuffle(allOtherUsers);
//						for (String user : allOtherUsers) {
//							sleep(500);
//							if (session.sendMessageToUser(new RequestFileMessage(user, name), user)) {
//								break;
//							}
//						}
//					}

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

		session.onMessageReceived(ElementRemovedMessage.class, message -> {
			gameTableElements.remove(gameTableElements.getById(message.getId()));
		});

		session.onMessageReceived(ElementToTopMessage.class, message -> {
			gameTableElements.getById(message.getId()).toFront();
		});

		session.onMessageReceived(ElementToBackMessage.class, message -> {
			gameTableElements.getById(message.getId()).toBack();
		});

		session.onMessageReceived(ElementRotatedMessage.class, message -> {
			new RotateTransition(gameTableElements.getById(message.getId()), message.getAngle()).play();
		});

		session.onMessageReceived(RequestFileMessage.class, message -> {
			// if the owner of the file is me i have to send it
			// since this usually happens directly after an image is added, only
			// the user who added it on the gametable has the file
			if (message.getOwner().equals(session.getUserName())) {
				session.sendFile(message.getSender(), new File(IMAGE_FOLDER + message.getFileName()));
			}
		});

		session.onSessionEvent(FileReceivedEvent.class, event -> {
			List<Node> byFilename = gameTableElements.getByFilename(event.getFile().getName());
			for (Node node : byFilename) {
				((ImageNode) node).setImage(new Image(FILE + IMAGE_FOLDER + event.getFile().getName()));
			}
		});

		session.onSessionEvent(UserJoinedEvent.class, event -> {
			String joinedUser = event.getName();
			List<Node> my = gameTableElements.getOwnedBy(session.getUserName());
			// TODO what happens with elements from a user that has left the
			// session?
			// find a better way to determine the users that send the elements
			// to a new one
			for (Node node : my) {
				if (node instanceof Circle) {
					MoveableCircle circle = (MoveableCircle) node;
					session.sendMessageToUser(new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(),
							circle.getY(), circle.getRadius(), circle.getRadius()), joinedUser);
				} else if (node instanceof ImageNode) {
					ImageNode imageNode = (ImageNode) node;
					session.sendMessageToUser(new ElementAddedMessage(imageNode.getId(), ElementType.IMAGE,
							imageNode.getX(), imageNode.getY(), imageNode.getWidth(), imageNode.getHeight()), joinedUser);
				}
				sleep(500);

				if (node.getRotate() != 0) {
					session.sendMessageToUser(new ElementRotatedMessage(node.getId(), (int) node.getRotate()), joinedUser);
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
