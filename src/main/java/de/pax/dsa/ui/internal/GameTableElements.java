package de.pax.dsa.ui.internal;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;

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

	@Inject
	private Logger logger;

	@PostConstruct
	public void postConstruct(Pane pane) {
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
			logger.error("ID: " + id + "exists not exactly once (" + collect.size() + " times)");
			group.getChildren().stream().forEach(node -> {
				logger.error("available ids are: " + node.getId());

			});
			return null;
		} else {
			return collect.get(0);
		}
	}

	public List<Node> getByFilename(String filename) {
		return group.getChildren()//
				.stream()//
				.filter(e -> filename.equals(IdBuilder.getName(e.getId())))//
				.collect(Collectors.toList());
	}

	public List<Node> getOwnedBy(String userName) {
		return group.getChildren()//
				.stream()//
				.filter(e -> userName.equals(IdBuilder.getOwner(e.getId())))//
				.collect(Collectors.toList());
	}
}
