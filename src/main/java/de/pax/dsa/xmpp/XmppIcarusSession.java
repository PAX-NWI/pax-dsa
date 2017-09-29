package de.pax.dsa.xmpp;

import java.io.IOException;
import java.util.function.Consumer;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.StringConverter;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import javafx.application.Platform;

/**
 * Created by swinter on 22.04.2017.
 */
public class XmppIcarusSession implements IIcarusSession {
	private static final String SERVER = "jabber.de";

	private Logger logger = LoggerFactory.getLogger(XmppIcarusSession.class);

	private XmppManager xmppManager;

	private Consumer<PositionUpdatedMessage> positionUpdateConsumer;

	private Consumer<ElementAddedMessage> onElementAddedConsumer;

	private String user;

	@Override
	public void connect(String user, String password) {
		this.user = user;
		try {
			xmppManager = new XmppManager(SERVER, user, password);
		} catch (XMPPException | IOException | InterruptedException | SmackException e) {
			logger.error("Unable to connect to " + SERVER, e);
		}
		xmppManager.addMessageListener(message -> {
			Platform.runLater(() -> {
				Object decode = StringConverter.decode(message.getBody());
				if (decode instanceof PositionUpdatedMessage) {
					positionUpdateConsumer.accept((PositionUpdatedMessage) decode);
				} else if (decode instanceof ElementAddedMessage) {
					onElementAddedConsumer.accept((ElementAddedMessage) decode);
				} else {
					logger.warn("Received non decodable message: " + message);
				}
			});
		});
	}

	@Override
	public void sendPositionUpdate(PositionUpdatedMessage positionUpdatedMessage) {
		xmppManager.sendMessage(positionUpdatedMessage.toString());
	}

	@Override
	public void onPositionUpdate(Consumer<PositionUpdatedMessage> positionUpdateConsumer) {
		this.positionUpdateConsumer = positionUpdateConsumer;
	}

	@Override
	public void sendElementAdded(ElementAddedMessage elementAddedMessage) {
		xmppManager.sendMessage(elementAddedMessage.toString());
	}

	@Override
	public void onElementAdded(Consumer<ElementAddedMessage> onElementAddedConsumer) {
		this.onElementAddedConsumer = onElementAddedConsumer;
	}

	@Override
	public void disconnect() {
		xmppManager.disconnect();
	}

	@Override
	public String getUserName() {
		return user;
	}

}
