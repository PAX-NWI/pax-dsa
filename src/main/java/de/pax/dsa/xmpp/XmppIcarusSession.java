package de.pax.dsa.xmpp;

import java.io.IOException;
import java.util.function.Consumer;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.connection.StringConverter;
import de.pax.dsa.model.messages.PositionUpdatedMessage;

/**
 * Created by swinter on 22.04.2017.
 */
public class XmppIcarusSession implements IIcarusSession {
	private static final String SERVER = "jabber.de";

	private Logger logger = LoggerFactory.getLogger(XmppIcarusSession.class);

	private XmppManager xmppManager;

	private Consumer<PositionUpdatedMessage> positionUpdateConsumer;

	@Override
	public void connect(String user, String password) {
		try {
			xmppManager = new XmppManager(SERVER, user, password);
		} catch (XMPPException | IOException | InterruptedException | SmackException e) {
			logger.error("Unable to connect to " + SERVER, e);
		}
		xmppManager.addMessageListener(message -> {
			Object decode = StringConverter.decode(message.getBody());
			if (decode instanceof PositionUpdatedMessage) {
				positionUpdateConsumer.accept((PositionUpdatedMessage) decode);
			} else {
				logger.warn("Received non decodable message: " + message);
			}
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
	
}
