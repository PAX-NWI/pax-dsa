package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.StringMapper;

public class ElementToBackMessage implements IMessageObject {

	private String id;
	private String sender;

	public ElementToBackMessage(String id) {
		this.id = id;
	}

	public ElementToBackMessage(Message message, String sender) {
		this.sender = sender;
		Map<String, String> map = StringMapper.keyValueListStringToMap(message.getBody());
		this.id = map.get("id");
	}

	public String getId() {
		return id;
	}

	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public String toString() {
		return "ElementToBack [id=" + id + "]";
	}

	public static String startsWith() {
		return "ElementToBack [";
	}

}
