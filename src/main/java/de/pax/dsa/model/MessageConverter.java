package de.pax.dsa.model;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;

public class MessageConverter {

	public static Object decode(Message message, String sender) {
		String body = message.getBody();
		if (body.startsWith(PositionUpdatedMessage.startsWith())) {
			return new PositionUpdatedMessage(message, sender);
		} else if (body.startsWith(ElementAddedMessage.startsWith())) {
			return new ElementAddedMessage(message, sender);
		} else if (body.startsWith(RequestFileMessage.startsWith())) {
			return new RequestFileMessage(message, sender);
		} else if (body.startsWith(ElementRemovedMessage.startsWith())) {
			return new ElementRemovedMessage(message, sender);
		}
		return null;
	}

}
