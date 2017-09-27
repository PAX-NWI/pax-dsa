package de.pax.dsa.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringMapper {

	/**
	 * Takes a string like "Trallala [id=id, x=52.0, y=42.0]" an converts it
	 * into an usable Map of the contained key=value pairs
	 */
	public static Map<String, String> keyValueListStringToMap(String string) {

		HashMap<String, String> map = new HashMap<>();

		Pattern p = Pattern.compile("\\[(.*?)\\]");
		Matcher m = p.matcher(string);

		m.find();

		String elements = m.group(0).replaceAll("\\[|\\]", "");
		String[] split = elements.split(",");
		for (String keyValuePair : split) {
			String[] pair = keyValuePair.split("=");
			map.put(pair[0].trim(), pair[1].trim());
		}

		return map;
	}

}
