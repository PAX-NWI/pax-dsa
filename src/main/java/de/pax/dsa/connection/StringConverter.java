package de.pax.dsa.connection;

import de.pax.dsa.model.PositionUpdate;

public class StringConverter {

	public static Object decode(String string) {

		if (string.startsWith("PositionUpdate")) {
			return new PositionUpdate(string);
		}

		return null;
	}

}
