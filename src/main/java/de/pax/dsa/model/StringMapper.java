package de.pax.dsa.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringMapper {

	/**
	 * Takes a string like "Trallala [id=id, x=52.0, y=42.0]" an converts it
	 * into an Map of the contained key=value pairs
	 */
	public static Map<String, String> keyValueListStringToMap(String string) {

		HashMap<String, String> map = new HashMap<>();

		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(string);

		matcher.find();

		String[] keyValueStrings = matcher.group(0).replaceAll("\\[|\\]| ", "").split(",");
		for (String keyValueString : keyValueStrings) {
			String[] keyValue = keyValueString.split("=");
			map.put(keyValue[0], keyValue[1]);
		}
		return map;
	}

}
