package de.pax.dsa.ui.internal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementToBackMessage;
import de.pax.dsa.model.messages.ElementToTopMessage;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseEvent;
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

			addContextMenu(img);

			session.sendMessage(
					new ElementAddedMessage(identifier, ElementType.IMAGE, x, y, img.getWidth(), img.getHeight()));
		});

		pane.setOnDragDropped(dnd::mouseDragDropped);
		pane.setOnDragOver(dnd::mouseDragOver);

		registerToSessionEvents();

		ContextMenu contextMenu = new ContextMenu();
		pane.setOnMousePressed(event -> {
			if (event.isSecondaryButtonDown()) {
				Image image = Clipboard.getSystemClipboard().getImage();
				if (image != null) {
					double screenX = event.getScreenX();
					double screenY = event.getScreenY();
					contextMenu.show(pane, screenX, screenY - 30);
				}
			} else {
				contextMenu.hide();
			}
		});

		MenuItem onTopItem = new MenuItem("Paste Image");
		contextMenu.getItems().addAll(onTopItem);
		onTopItem.setOnAction(event -> {
			Image image = Clipboard.getSystemClipboard().getImage();
			if (image == null) {
				return;
			}

			String filename = "pasted-" + IdBuilder.getTimeStamp() + ".png";
			File outputFile = new File("images/" + filename);
			BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
			try {
				ImageIO.write(bImage, "png", outputFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			String identifier = IdBuilder.build(filename, session.getUserName());
			double x = contextMenu.getX();
			double y = contextMenu.getY();

			Point2D translated = pane.screenToLocal(x, y);

			x = translated.getX();
			y = translated.getY();

			ImageNode img = new ImageNode(identifier, image, x, y);
			DragEnabler.enableDrag(img, session::sendMessage);
			gameTableElements.add(img);

			addContextMenu(img);

			session.sendMessage(
					new ElementAddedMessage(identifier, ElementType.IMAGE, x, y, img.getWidth(), img.getHeight()));
		});

	}

	private void addContextMenu(Node master) {
		ContextMenuProvider context = new ContextMenuProvider(master);
		context.addEntry("Delete", node -> {
			session.sendMessage(new ElementRemovedMessage(node.getId()));
			gameTableElements.remove(node);
		});
		context.addEntry("To Top", node -> {
			session.sendMessage(new ElementToTopMessage(node.getId()));
			node.toFront();
		});
		context.addEntry("To Back", node -> {
			session.sendMessage(new ElementToBackMessage(node.getId()));
			node.toBack();
		});
	}

	public void addCircle() {
		MoveableCircle circle = new MoveableCircle(IdBuilder.build("circle", session.getUserName()), 50, 50, 10);
		gameTableElements.add(circle);
		DragEnabler.enableDrag(circle, session::sendMessage);

		addContextMenu(circle);

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
				addContextMenu(newCircle);
			} else if (elementType == ElementType.IMAGE) {
				String id = message.getId();
				String name = IdBuilder.getName(id);
				ImageNode imageNode = null;
				if (!new File(IMAGE_FOLDER + name).exists()) {
					String owner = IdBuilder.getOwner(id);
					session.sendMessage(new RequestFileMessage(owner, name));
					imageNode = new ImageNode(id, FILE + IMAGE_FOLDER + "waiting.gif", message.getX(), message.getY());
				} else {
					imageNode = new ImageNode(id, FILE + IMAGE_FOLDER + name, message.getX(), message.getY());
				}
				gameTableElements.add(imageNode);
				DragEnabler.enableDrag(imageNode, sendPositionUpdate);
				addContextMenu(imageNode);
			} else {
				logger.warn("Unknown element type {}", elementType);
			}
		});

		session.onElementRemoved(elementRemovedMessage -> {
			gameTableElements.remove(gameTableElements.getById(elementRemovedMessage.getId()));
		});

		session.onElementToTop(elementToTopMessage -> {
			gameTableElements.getById(elementToTopMessage.getId()).toFront();
		});

		session.onElementToBack(elementToBackMessage -> {
			gameTableElements.getById(elementToBackMessage.getId()).toBack();
		});

		session.onRequestFile(t -> {
			// if the owner of the file is me i have to send it
			if (t.getOwner().equals(session.getUserName())) {
				session.sendFile(t.getSender(), new File(IMAGE_FOLDER + t.getFileName()));
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
