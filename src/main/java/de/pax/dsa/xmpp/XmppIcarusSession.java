package de.pax.dsa.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.IUiSynchronize;
import de.pax.dsa.model.MessageConverter;
import de.pax.dsa.model.messages.IMessage;

/**
 * Created by swinter on 22.04.2017.
 */
public class XmppIcarusSession implements IIcarusSession {
	private static final String SERVER = "jabber.de";

	private Logger logger = LoggerFactory.getLogger(XmppIcarusSession.class);

	private XmppManager xmppManager;

	private String user;

	private Consumer<File> onFileReceivedConsumer;

	Map<Class<?>, Consumer<?>> messageConsumerList = new HashMap<>();

	private Consumer<String> onUserEnteredConsumer;
	
	@Inject
	private IUiSynchronize uiSynchronize;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void connect(String user, String password) {
		this.user = user;
		try {
			xmppManager = new XmppManager(SERVER, user, password, uiSynchronize);
		} catch (XMPPException | IOException | InterruptedException | SmackException e) {
			logger.error("Unable to connect to " + SERVER, e);
		}
		xmppManager.addMessageListener(message -> {
			Resourcepart sender = message.getFrom().getResourceOrThrow();
			if (sender.toString().equals(user)) {
				// do not listen to own messages
				return;
			}

			uiSynchronize.run(() -> {
				logger.info("Received message:" + message.getBody());
				Object decode = MessageConverter.decode(message, sender.toString());
				Class<? extends Object> class1 = decode.getClass();
				Consumer consumer = messageConsumerList.get(class1);
				if (consumer != null) {
					consumer.accept(decode);
				} else {
					logger.warn("No Consumer registered for {}", class1);
				}

			});
		});

		xmppManager.onFileReceived(onFileReceivedConsumer);
		xmppManager.onUserEntered(onUserEnteredConsumer);
	}

	@Override
	public void sendMessage(IMessage message) {
		if (xmppManager != null) {
			xmppManager.sendMessage(message.toString());
		} else {
			logger.warn("Not connected, can't send {}", message.toString());
		}
	}

	@Override
	public <T> void onMessageReceived(Class<T> messageClass, Consumer<T> consumer) {
		if (messageConsumerList.containsKey(messageClass)) {
			logger.warn("Consumer for class {} already registered and will be overwritten!", messageClass);
		}
		messageConsumerList.put(messageClass, consumer);
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
	public void onFileReceived(Consumer<File> onFileReceivedConsumer) {
		this.onFileReceivedConsumer = onFileReceivedConsumer;
	}

	@Override
	public void onUserEntered(Consumer<String> onUserEnteredConsumer) {
		this.onUserEnteredConsumer = onUserEnteredConsumer;
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

	@Override
	public void sendMessageToUser(IMessage message, String name) {
		try {
			xmppManager.sendMessage(message.toString(), name);
		} catch (XmppStringprepException | NotConnectedException | XMPPException | InterruptedException e) {
			logger.error("Error sending provate message to " + name, e);
		}

	}

}
