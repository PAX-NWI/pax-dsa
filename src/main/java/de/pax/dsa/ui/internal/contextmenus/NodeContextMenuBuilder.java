package de.pax.dsa.ui.internal.contextmenus;

import javax.inject.Inject;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementToBackMessage;
import de.pax.dsa.model.messages.ElementToTopMessage;
import de.pax.dsa.ui.internal.GameTableElements;
import javafx.scene.Node;
/**
 * The default Context Menu that is available on all nodes
 * @author alex
 *
 */
public class NodeContextMenuBuilder {

	@Inject
	private IIcarusSession session;
	
	@Inject
	private GameTableElements gameTableElements;

	public void buildContextMenuFor(Node master) {
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

}
