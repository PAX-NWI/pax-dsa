package de.pax.dsa.ui.internal.contextmenus;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.ui.internal.GameTableElements;
import de.pax.dsa.ui.internal.dragsupport.DragEnabler;
import de.pax.dsa.ui.internal.dragsupport.IdBuilder;
import de.pax.dsa.ui.internal.nodes.ImageNode;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.Pane;

/**
 * Builds the Context Menu on the Gametable Pane
 * 
 * @author alex
 */
public class PaneContextMenuBuilder {

	@PostConstruct
	private void postConstruct(Pane pane, IIcarusSession session, GameTableElements gameTableElements,
			NodeContextMenuBuilder nodeContextMenuBuilder) {

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
			Point2D point = pane.screenToLocal(contextMenu.getX(), contextMenu.getY());

			ImageNode imageNode = new ImageNode(identifier, image, point.getX(), point.getY());
			DragEnabler.enableDrag(imageNode, session::sendMessage);
			DragEnabler.enableRotate(imageNode.getRotateAnchor(), imageNode, session::sendMessage);
			
			gameTableElements.add(imageNode);

			nodeContextMenuBuilder.buildContextMenuFor(imageNode);

			session.sendMessage(new ElementAddedMessage(identifier, ElementType.IMAGE, point.getX(), point.getY(),
					imageNode.getWidth(), imageNode.getHeight()));
		});

	}

}
