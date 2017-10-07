package de.pax.dsa.ui.internal;

import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class ContextMenuProvider {

	private ContextMenu contextMenu;
	private Node node;

	public ContextMenuProvider(Node node) {
		this.node = node;
		contextMenu = new ContextMenu();
		node.setOnMousePressed(event -> {
			if (event.isSecondaryButtonDown()) {
				contextMenu.show(node, event.getScreenX(), event.getScreenY());
			}
		});
	}

	public void onDelete(Consumer<Node> deleteHandler) {
		MenuItem delete = new MenuItem("Delete");
		contextMenu.getItems().addAll(delete);
		delete.setOnAction(event -> deleteHandler.accept(node));
	}

}
