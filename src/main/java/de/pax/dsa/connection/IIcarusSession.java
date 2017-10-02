package de.pax.dsa.connection;

import java.io.File;
import java.util.function.Consumer;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;

public interface IIcarusSession {

	void connect(String user, String password);
	
	void disconnect();

	void onPositionUpdate(Consumer<PositionUpdatedMessage> positionUpdateConsumer);

	void sendPositionUpdate(PositionUpdatedMessage positionUpdatedMessage);

	void sendElementAdded(ElementAddedMessage elementAddedMessage);

	void onElementAdded(Consumer<ElementAddedMessage> positionUpdateConsumer);

	String getUserName();

	String getServer();

	void sendRequestFile(RequestFileMessage requestFileMessage);

	void onRequestFile(Consumer<RequestFileMessage> onRequestFileConsumer);

	void onFileReceived(Consumer<File> onFileReceivedConsumer);

	void sendFile(String buddyJID, File file);

}
