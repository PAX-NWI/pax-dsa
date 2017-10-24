package de.pax.dsa.connection;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import de.pax.dsa.model.messages.IMessageObject;
import de.pax.dsa.model.sessionEvents.ISessionEvent;

public interface IIcarusSession {

	void connect(String user, String password);

	void disconnect();

	String getUserName();

	String getServer();

	List<String> getAllOtherUsers();

	void sendFile(String buddyJID, File file);

	void sendMessage(IMessageObject message);

	boolean sendMessageToUser(IMessageObject message, String name);

	<T extends IMessageObject> void onMessageReceived(Class<T> messageClass, Consumer<T> consumer);

	<T extends ISessionEvent> void onSessionEvent(Class<T> sessionEventClass, Consumer<T> consumer);
}
