package de.pax.dsa.connection;

import java.io.File;
import java.util.function.Consumer;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementToBackMessage;
import de.pax.dsa.model.messages.ElementToTopMessage;
import de.pax.dsa.model.messages.IMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;

public interface IIcarusSession {

	void connect(String user, String password);
	
	void disconnect();

	void onPositionUpdate(Consumer<PositionUpdatedMessage> positionUpdateConsumer);

	void onElementAdded(Consumer<ElementAddedMessage> positionUpdateConsumer);

	String getUserName();

	String getServer();

	void onRequestFile(Consumer<RequestFileMessage> onRequestFileConsumer);

	void onFileReceived(Consumer<File> onFileReceivedConsumer);

	void sendFile(String buddyJID, File file);

	void sendMessage(IMessage message);

	void onElementRemoved(Consumer<ElementRemovedMessage> onElementRemovedConsumer);

	void onElementToTop(Consumer<ElementToTopMessage> onElementToTopConsumer);

	void onElementToBack(Consumer<ElementToBackMessage> onElementToBackConsumer);

}
 