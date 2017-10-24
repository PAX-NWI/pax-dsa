package de.pax.dsa.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

		Pattern pattern = Pattern.compile("\\[(.*?)\\]$");
		Matcher matcher = pattern.matcher(string);

		matcher.find();

		String inSquareBrackets = matcher.group(0);
		String withoutBrackets = inSquareBrackets.substring(1, inSquareBrackets.length() - 1);

		// split on the comma only if that comma has zero, or an even number of
		// quotes ahead of it.
		// String[] keyValueStrings =
		// withoutBrackets.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

		List<String> keyValueStrings = commaSplit(withoutBrackets);

		for (String keyValueString : keyValueStrings) {
			String[] keyValue = keyValueString.split("=", 2);
			String trimmedKey = keyValue[0].trim();
			map.put(trimmedKey, keyValue[1]);
		}
		return map;
	}

	/**
	 * splitting a comma-separated string but ignoring commas in quotes
	 * https://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
	 */
	private static List<String> commaSplit(String toSplit) {
		List<String> tokensList = new ArrayList<>();
		boolean inQuotes = false;
		StringBuilder b = new StringBuilder();
		for (char c : toSplit.toCharArray()) {
			switch (c) {
			case ',':
				if (inQuotes) {
					b.append(c);
				} else {
					tokensList.add(b.toString());
					b = new StringBuilder();
				}
				break;
			case '\"':
				inQuotes = !inQuotes;
			default:
				b.append(c);
				break;
			}
		}
		tokensList.add(b.toString());
		return tokensList;
	}

}
