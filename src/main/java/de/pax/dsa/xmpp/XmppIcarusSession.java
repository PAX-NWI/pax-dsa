package de.pax.dsa.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.parts.Resourcepart;
import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.IUiSynchronize;
import de.pax.dsa.model.MessageConverter;
import de.pax.dsa.model.messages.IMessageObject;
import de.pax.dsa.model.sessionEvents.ISessionEvent;
import de.pax.dsa.model.sessionEvents.SessionConnectedEvent;

/**
 * Created by swinter on 22.04.2017.
 */
public class XmppIcarusSession implements IIcarusSession {
	private static final String SERVER = "jabber.de";

	private XmppManager xmppManager;

	private String user = "notConnected";

	private Map<Class<? extends IMessageObject>, Consumer<? extends IMessageObject>> messageConsumerList = new HashMap<>();

	private Map<Class<ISessionEvent>, List<Consumer<ISessionEvent>>> sessionEventConsumerList = new HashMap<>();

	@Inject
	private Logger logger;

	@Inject
	private IUiSynchronize uiSynchronize;

	@Override
	public void connect(String user, String password) {

		this.user = user;
		try {
			xmppManager = new XmppManager(SERVER, user, password, uiSynchronize);
		} catch (XMPPException | IOException | InterruptedException | SmackException e) {
			logger.error("Unable to connect to " + SERVER, e);
		}
		xmppManager.addMessageListener(this::processMessage);

		xmppManager.addSessionEventListener(this::processSessionEvent);

		processSessionEvent(new SessionConnectedEvent(true));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void processMessage(Message message) {
		Resourcepart sender = message.getFrom().getResourceOrThrow();
		if (sender.toString().equals(user)) {
			logger.debug("Ignoring message sent by myself: " + message.getBody());
			return;
		}
		uiSynchronize.run(() -> {
			logger.debug("Received message:" + message.getBody());
			Object decode = MessageConverter.decode(message, sender.toString());
			Class<? extends Object> class1 = decode.getClass();
			Consumer consumer = messageConsumerList.get(class1);
			if (consumer != null) {
				consumer.accept(decode);
			} else {
				logger.warn("No Consumer registered for {}", class1);
			}
		});
	}

	private void processSessionEvent(ISessionEvent event) {
		uiSynchronize.run(() -> {
			List<Consumer<ISessionEvent>> consumers = sessionEventConsumerList.get(event.getClass());
			if (consumers != null) {
				consumers.forEach(consumer -> consumer.accept(event));
			}
		});
	}

	@Override
	public void sendMessage(IMessageObject message) {
		if (xmppManager != null) {
			xmppManager.sendMessage(message.toString());
		} else {
			logger.warn("Not connected, can't send {}", message.toString());
		}
	}

	@Override
	public boolean sendMessageToUser(IMessageObject message, String name) {
		if (xmppManager != null) {
			return xmppManager.sendMessage(message.toString(), name);
		} else {
			logger.warn("Not connected, can't send private message {} to {}", message.toString(), name);
			return true;
			// Returning false here would potentially cause the client to send
			// messages to other users instead because he thinks that only this
			// user is not online.
			// But since we now here that we are not connected and throw a
			// warning, we return true;
		}
	}

	@Override
	public <T extends IMessageObject> void onMessageReceived(Class<T> messageClass, Consumer<T> consumer) {
		if (messageConsumerList.containsKey(messageClass)) {
			logger.warn("Consumer for class {} already registered and will be overwritten!", messageClass);
		}
		messageConsumerList.put(messageClass, consumer);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ISessionEvent> void onSessionEvent(Class<T> sessionEventClass, Consumer<T> consumer) {
		if (!sessionEventConsumerList.containsKey(sessionEventClass)) {
			sessionEventConsumerList.put((Class<ISessionEvent>) sessionEventClass, new ArrayList<>());
		}
		sessionEventConsumerList.get(sessionEventClass).add((Consumer<ISessionEvent>) consumer);
	}

	@Override
	public void sendFile(String buddyJID, File file) {
		if (xmppManager != null) {
			xmppManager.sendFile(buddyJID, file);
		} else {
			logger.warn("Not connected, can't send File");
		}
	}

	@Override
	public List<String> getAllOtherUsers() {
		if (xmppManager != null) {
			return xmppManager.getAllOtherUsers();
		} else {
			logger.warn("Not connected, can't get all other users");
			return new ArrayList<>();
		}

	}

	@Override
	public void disconnect() {
		if (xmppManager != null) {
			xmppManager.disconnect();
		}
	}

	@Override
	public String getUserName() {
		return user;
	}

	@Override
	public String getServer() {
		return SERVER;
	}

}
