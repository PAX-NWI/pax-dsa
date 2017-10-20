package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.StringMapper;

public class ElementRotatedMessage implements IMessageObject {

	private String id;
	private String sender;
	private int angle;

	public ElementRotatedMessage(String id, int angle) {
		this.id = id;
		this.angle = angle;
	}

	public ElementRotatedMessage(Message message, String sender) {
		this.sender = sender;
		Map<String, String> map = StringMapper.keyValueListStringToMap(message.getBody());
		this.id = map.get("id");
		this.angle = Integer.parseInt(map.get("angle"));
	}

	public String getId() {
		return id;
	}

	public int getAngle() {
		return angle;
	}

	@Override
	public String getSender() {
		return sender;
	}

	@Override
	public String toString() {
		return "ElementRotated [id=" + id + ", angle=" + angle + "]";
	}

	public static String startsWith() {
		return "ElementRotated [";
	}

}
