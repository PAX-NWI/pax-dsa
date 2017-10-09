package de.pax.dsa.ui.internal;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
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

	private static final String FILE = "file:";
	private static final String IMAGE_FOLDER = "images/";

	@Inject
	private IIcarusSession session;

	@Inject
	private Pane pane;

	@Inject
	private Logger logger;

	private TwoStageMoveNode nodeA;

	private TwoStageMoveNode nodeB;

	private GameTableElements gameTableElements;

	@PostConstruct
	private void posConstruct() {

		Canvas grid = GridFactory.createGrid(50);
		grid.setStyle("-fx-background-color: cornsilk;");
		pane.getChildren().add(grid);

		gameTableElements = new GameTableElements(pane);
		

		ImageDnDController dnd = new ImageDnDController(response -> {
			String identifier = IdBuilder.build(response.getName(), session.getUserName());
			double x = response.getX();
			double y = response.getY();
			ImageNode img = new ImageNode(identifier, response.getImage(), x, y);
			DragEnabler.enableDrag(img, session::sendMessage);
			gameTableElements.add(img);
			session.sendMessage(
					new ElementAddedMessage(identifier, ElementType.IMAGE, x, y, img.getWidth(), img.getHeight()));
		});

		pane.setOnDragDropped(dnd::mouseDragDropped);
		pane.setOnDragOver(dnd::mouseDragOver);

		registerToSessionEvents();
		
	}

	public void addCircle() {
		MoveableCircle circle = new MoveableCircle(IdBuilder.build("circle", session.getUserName()), 50, 50, 10);
		gameTableElements.add(circle);
		DragEnabler.enableDrag(circle, session::sendMessage);

		
		
		ContextMenuProvider context = new ContextMenuProvider(circle);
		context.onDelete(node -> {
			String id = node.getId();
			session.sendMessage(new ElementRemovedMessage(id));
			gameTableElements.remove(node);
		});

		session.sendMessage(new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(), circle.getY(),
				circle.getRadius(), circle.getRadius()));
	}

	private void registerToSessionEvents() {
		Consumer<PositionUpdatedMessage> sendPositionUpdate = session::sendMessage;

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
			new Move2DTransition((I2DObject) node, positionUpdate.getX(), positionUpdate.getY(), 2).play();
		});

		session.onElementAdded(message -> {
			ElementType elementType = message.getElementType();
			if (elementType == ElementType.CIRCLE) {
				MoveableCircle newCircle = new MoveableCircle(message.getId(), message.getX(), message.getY(),
						message.getW());
				gameTableElements.add(newCircle);
				DragEnabler.enableDrag(newCircle, sendPositionUpdate);
			} else if (elementType == ElementType.IMAGE) {
				String id = message.getId();
				String name = IdBuilder.getName(id);
				ImageNode imageNode = null;
				if (!new File(IMAGE_FOLDER + name).exists()) {
					String owner = IdBuilder.getOwner(id);
					session.sendMessage(new RequestFileMessage(owner, name, session.getUserName()));
					imageNode = new ImageNode(id, FILE + IMAGE_FOLDER + "waiting.gif", message.getX(), message.getY());
				} else {
					imageNode = new ImageNode(id, FILE + IMAGE_FOLDER + name, message.getX(), message.getY());
				}
				gameTableElements.add(imageNode);
				DragEnabler.enableDrag(imageNode, sendPositionUpdate);
			} else {
				logger.warn("Unknown element type {}", elementType);
			}
		});
		
		session.onElementRemoved(elementRemovedMessage -> {
			gameTableElements.remove(gameTableElements.getById(elementRemovedMessage.getId()));
		});

		session.onRequestFile(new Consumer<RequestFileMessage>() {

			@Override
			public void accept(RequestFileMessage t) {
				// if the owner of the file is me i have to send it
				if (t.getOwner().equals(session.getUserName())) {
					session.sendFile(t.getRequester(), new File(IMAGE_FOLDER + t.getFileName()));
				}

			}
		});

		session.onFileReceived(f -> {
			List<Node> byFilename = gameTableElements.getByFilename(f.getName());
			for (Node node : byFilename) {
				((ImageNode) node).setImage(new Image(FILE + IMAGE_FOLDER + f.getName()));
			}
		});

	}

	public void doMoves() {
		nodeA.commitMove();
		nodeB.commitMove();
	}



}
