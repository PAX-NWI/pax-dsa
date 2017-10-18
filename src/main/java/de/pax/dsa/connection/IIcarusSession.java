package de.pax.dsa.connection;

import java.io.File;
import java.util.function.Consumer;

import de.pax.dsa.model.messages.IMessage;

public interface IIcarusSession {

	void connect(String user, String password);
	
	void disconnect();

	String getUserName();

	String getServer();

	void onFileReceived(Consumer<File> onFileReceivedConsumer);

	void sendFile(String buddyJID, File file);

	void sendMessage(IMessage message);

	void onUserEntered(Consumer<String> onUserEnteredConsumer);

	void sendMessageToUser(IMessage message, String name);

	<T> void onMessageReceived(Class<T> messageClass, Consumer<T> consumer);

}
 