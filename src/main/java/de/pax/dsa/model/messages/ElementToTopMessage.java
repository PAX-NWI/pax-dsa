package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.StringMapper;

public class ElementToTopMessage implements IMessage {

	private String id;
	private String sender;

	public ElementToTopMessage(String id) {
		this.id = id;
	}

	public ElementToTopMessage(Message message, String sender) {
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
		return "ElementToTop [id=" + id + "]";
	}

	public static String startsWith() {
		return "ElementToTop [";
	}

}
