package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.StringMapper;

public class TextMessage implements IMessageObject {

	private String text;
	private String sender;

	public TextMessage(String text) {
		this.text = text;
	}

	public TextMessage(Message message, String sender) {
		this.sender = sender;
		Map<String, String> map = StringMapper.keyValueListStringToMap(message.getBody());
		this.text = map.get("text");
	}

	public String getText() {
		return text;
	}

	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public String toString() {
		return "TextMessage [text=" + text + "]";
	}

	public static String startsWith() {
		return "TextMessage [";
	}

}
