package de.pax.dsa.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jxmpp.jid.parts.Resourcepart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.MessageConverter;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementRotatedMessage;
import de.pax.dsa.model.messages.ElementToBackMessage;
import de.pax.dsa.model.messages.ElementToTopMessage;
import de.pax.dsa.model.messages.IMessage;
import de.pax.dsa.model.messages.ElementMovedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;
import javafx.application.Platform;

/**
 * Created by swinter on 22.04.2017.
 */
public class XmppIcarusSession implements IIcarusSession {
	private static final String SERVER = "jabber.de";

	private Logger logger = LoggerFactory.getLogger(XmppIcarusSession.class);

	private XmppManager xmppManager;

	private Consumer<ElementMovedMessage> positionUpdateConsumer;

	private Consumer<ElementAddedMessage> onElementAddedConsumer;

	private String user;

	private Consumer<RequestFileMessage> onRequestFileConsumer;

	private Consumer<File> onFileReceivedConsumer;

	private Consumer<ElementRemovedMessage> onElementRemovedConsumer;

	private Consumer<ElementToTopMessage> onElementToTopConsumer;

	private Consumer<ElementToBackMessage> onElementToBackConsumer;

	private Consumer<ElementRotatedMessage> onElementRotatedConsumer;

	@Override
	public void connect(String user, String password) {
		this.user = user;
		try {
			xmppManager = new XmppManager(SERVER, user, password);
		} catch (XMPPException | IOException | InterruptedException | SmackException e) {
			logger.error("Unable to connect to " + SERVER, e);
		}
		xmppManager.addMessageListener(message -> {
			Resourcepart sender = message.getFrom().getResourceOrEmpty();
			if (sender.equals(user)) {
				// do not listen to own messages
				return;
			}

			Platform.runLater(() -> {
				logger.info("Received message:" + message.getBody());
				Object decode = MessageConverter.decode(message, sender.toString());
				if (decode instanceof ElementMovedMessage) {
					positionUpdateConsumer.accept((ElementMovedMessage) decode);
				} else if (decode instanceof ElementAddedMessage) {
					onElementAddedConsumer.accept((ElementAddedMessage) decode);
				} else if (decode instanceof RequestFileMessage) {
					onRequestFileConsumer.accept((RequestFileMessage) decode);
				} else if (decode instanceof ElementRemovedMessage) {
					onElementRemovedConsumer.accept((ElementRemovedMessage) decode);
				} else if (decode instanceof ElementToTopMessage) {
					onElementToTopConsumer.accept((ElementToTopMessage) decode);
				} else if (decode instanceof ElementToBackMessage) {
					onElementToBackConsumer.accept((ElementToBackMessage) decode);
				} else if (decode instanceof ElementRotatedMessage) {
					onElementRotatedConsumer.accept((ElementRotatedMessage) decode);
				} 
				
				else {
					logger.warn("Received non decodable message: " + message);
				}
			});
		});

		xmppManager.onFileReceived(onFileReceivedConsumer);
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
	public void onPositionUpdate(Consumer<ElementMovedMessage> positionUpdateConsumer) {
		this.positionUpdateConsumer = positionUpdateConsumer;
	}

	@Override
	public void onElementAdded(Consumer<ElementAddedMessage> onElementAddedConsumer) {
		this.onElementAddedConsumer = onElementAddedConsumer;
	}

	@Override
	public void onRequestFile(Consumer<RequestFileMessage> onRequestFileConsumer) {
		this.onRequestFileConsumer = onRequestFileConsumer;
	}

	@Override
	public void onElementRemoved(Consumer<ElementRemovedMessage> onElementRemovedConsumer) {
		this.onElementRemovedConsumer = onElementRemovedConsumer;
	}

	@Override
	public void onElementToTop(Consumer<ElementToTopMessage> onElementToTopConsumer) {
		this.onElementToTopConsumer = onElementToTopConsumer;
	}

	@Override
	public void onElementToBack(Consumer<ElementToBackMessage> onElementToBackConsumer) {
		this.onElementToBackConsumer = onElementToBackConsumer;
	}
	
	@Override
	public void onElementRotated(Consumer<ElementRotatedMessage> onElementRotatedConsumer) {
		this.onElementRotatedConsumer = onElementRotatedConsumer;
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
