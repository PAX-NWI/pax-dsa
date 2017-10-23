package de.pax.dsa.connection;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import de.pax.dsa.model.messages.IMessageObject;

public interface IIcarusSession {

	void connect(String user, String password);
	
	void disconnect();

	String getUserName();

	String getServer();

	void onFileReceived(Consumer<File> onFileReceivedConsumer);

	void sendFile(String buddyJID, File file);

	void sendMessage(IMessageObject message);

	void onUserEntered(Consumer<String> onUserEnteredConsumer);

	boolean sendMessageToUser(IMessageObject message, String name);

	<T> void onMessageReceived(Class<T> messageClass, Consumer<T> consumer);

	List<String> getAllOtherUsers();

	void onSessionConnected(Consumer<Boolean> onSessionConnectedConsumer);

}
 