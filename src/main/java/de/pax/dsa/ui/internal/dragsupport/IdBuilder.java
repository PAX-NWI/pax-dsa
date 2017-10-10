package de.pax.dsa.ui.internal.dragsupport;

import java.sql.Timestamp;

/**
 * Manages creating and reading the identification string of all gametable objects which consists
 * of name, username, and timestamp combined in one string
 *
 */
public class IdBuilder {

	private static final String SEPARATOR = "-:-";

	public static String build(String name, String owner) {
		return name + SEPARATOR + owner + SEPARATOR + getTimeStamp();
	}

	public static String getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return Long.toString(timestamp.getTime());
	}

	public static String getName(String string) {
		String[] split = string.split(SEPARATOR);
		return split[0];
	}
	
	public static String getOwner(String string) {
		String[] split = string.split(SEPARATOR);
		return split[1];
	}

}
