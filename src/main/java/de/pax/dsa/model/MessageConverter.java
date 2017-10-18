package de.pax.dsa.model;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementRotatedMessage;
import de.pax.dsa.model.messages.ElementToBackMessage;
import de.pax.dsa.model.messages.ElementToTopMessage;
import de.pax.dsa.model.messages.IMessage;
import de.pax.dsa.model.messages.ElementMovedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;

public class MessageConverter {

	public static IMessage decode(Message message, String sender) {
		String body = message.getBody();
		if (body.startsWith(ElementMovedMessage.startsWith())) {
			return new ElementMovedMessage(message, sender);
		} else if (body.startsWith(ElementAddedMessage.startsWith())) {
			return new ElementAddedMessage(message, sender);
		} else if (body.startsWith(RequestFileMessage.startsWith())) {
			return new RequestFileMessage(message, sender);
		} else if (body.startsWith(ElementRemovedMessage.startsWith())) {
			return new ElementRemovedMessage(message, sender);
		} else if (body.startsWith(ElementToTopMessage.startsWith())) {
			return new ElementToTopMessage(message, sender);
		} else if (body.startsWith(ElementToBackMessage.startsWith())) {
			return new ElementToBackMessage(message, sender);
		} else if (body.startsWith(ElementRotatedMessage.startsWith())) {
			return new ElementRotatedMessage(message, sender);
		}
		return null;
	}

}
