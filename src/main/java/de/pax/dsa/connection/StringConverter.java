package de.pax.dsa.connection;

import de.pax.dsa.model.messages.PositionUpdatedMessage;

public class StringConverter {

	public static Object decode(String string) {

		if (string.startsWith("PositionUpdate")) {
			return new PositionUpdatedMessage(string);
		}

		return null;
	}

}
