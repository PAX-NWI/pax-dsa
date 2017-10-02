package de.pax.dsa.model;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;

public class MessageConverter {

	public static Object decode(String message) {

		if (message.startsWith(PositionUpdatedMessage.startsWith())) {
			return new PositionUpdatedMessage(message);
		} else if (message.startsWith(ElementAddedMessage.startsWith())) {
			return new ElementAddedMessage(message);
		} else if (message.startsWith(RequestFileMessage.startsWith())) {
			return new RequestFileMessage(message);
		}

		return null;
	}

}
