package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.StringMapper;

public class TextMessage implements IMessageObject {

	private String textInQuotes;
	private String sender;
	private String QUOTE = "\"";

	public TextMessage(String text) {
		this.textInQuotes = QUOTE + text + QUOTE;
	}

	public TextMessage(Message message, String sender) {
		this.sender = sender;
		Map<String, String> map = StringMapper.keyValueListStringToMap(message.getBody());
		this.textInQuotes = map.get("text");
	}

	public String getText() {
		return textInQuotes.substring(1, textInQuotes.length() - 1);
	}

	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public String toString() {
		return "TextMessage [text=" + textInQuotes + "]";
	}

	public static String startsWith() {
		return "TextMessage [";
	}

}
