package de.pax.dsa.ui.internal;

import java.util.List;
import java.util.stream.Collectors;

import de.pax.dsa.ui.internal.dragsupport.IdBuilder;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Manages all elements that are currently on the game table
 * 
 * @author alex
 *
 */
public class GameTableElements {

	private Group group;

	public GameTableElements(Pane pane) {
		group = new Group();
		pane.getChildren().add(group);
	}

	public void add(Node node) {
		group.getChildren().add(node);
	}

	public void remove(Node node) {
		group.getChildren().remove(node);
	}

	public Node getById(String id) {
		List<Node> collect = group.getChildren().stream().filter(e -> id.equals(e.getId()))
				.collect(Collectors.toList());
		if (collect.size() != 1) {
			System.err.println("searching: " + id);
			group.getChildren().stream().forEach(node -> {
				System.err.println("available: " + node.getId());

			});
			throw new IllegalStateException("ID: " + id + "exists not exactly once (" + collect.size() + " times)");
		} else {
			return collect.get(0);
		}
	}

	public List<Node> getByFilename(String filename) {
		List<Node> collect = group.getChildren().stream().filter(e -> filename.equals(IdBuilder.getName(e.getId())))
				.collect(Collectors.toList());
		return collect;

	}
}
