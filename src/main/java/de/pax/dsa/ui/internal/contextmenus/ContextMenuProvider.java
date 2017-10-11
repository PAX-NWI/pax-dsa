package de.pax.dsa.ui.internal.contextmenus;

import java.util.function.Consumer;

import de.pax.dsa.ui.internal.nodes.ImageNode;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ContextMenuProvider {

	private ContextMenu contextMenu;
	private Node node;
	private Node onContextMenuNode;

	public ContextMenuProvider(Node node) {
		this.node = node;
		onContextMenuNode = node;
		contextMenu = new ContextMenu();

		if (node instanceof ImageNode) {
			// hack: if it is an image node, registering an context menu on it
			// somehow interacts with the drag an drop mechanism and causes it
			// to move the images top left edge to the cursor position
			// to prevent this the context menu is registered on the image view
			// inside the imagenode directly
			onContextMenuNode = ((ImageNode) node).getImageView();
		}

		onContextMenuNode.setOnMousePressed(event -> {
			if (event.isSecondaryButtonDown()) {
				contextMenu.show(node, event.getScreenX(), event.getScreenY());
			} else {
				contextMenu.hide();
			}
		});
	}

	public void addEntry(String text, Consumer<Node> consumer) {
		MenuItem onTopItem = new MenuItem(text);
		contextMenu.getItems().addAll(onTopItem);
		onTopItem.setOnAction(event -> consumer.accept(node));
	}

}
