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
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		long time = timestamp.getTime();
		return name + SEPARATOR + owner + SEPARATOR + Long.toString(time);
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
