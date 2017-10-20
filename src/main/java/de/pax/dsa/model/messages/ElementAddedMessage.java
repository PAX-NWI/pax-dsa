package de.pax.dsa.model.messages;

import java.util.Map;

import org.jivesoftware.smack.packet.Message;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.StringMapper;

public class ElementAddedMessage implements IMessageObject {

	private ElementType elementType;
	private double x;
	private double y;
	private double w;
	private double h;
	private String id;
	private String sender;

	public ElementAddedMessage(String id, ElementType elementType, double x, double y, double w, double h) {
		this.id = id;
		this.elementType = elementType;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public ElementAddedMessage(Message message, String sender) {
		this.sender = sender;
		Map<String, String> map = StringMapper.keyValueListStringToMap(message.getBody());
		this.id = map.get("id");
		this.elementType = ElementType.valueOf(map.get("elementType"));
		this.x = Double.parseDouble(map.get("x"));
		this.y = Double.parseDouble(map.get("y"));
		this.w = Double.parseDouble(map.get("w"));
		this.h = Double.parseDouble(map.get("h"));
	}

	public String getId() {
		return id;
	}

	@Override
	public String getSender() {
		return sender;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getW() {
		return w;
	}

	public double getH() {
		return h;
	}

	@Override
	public String toString() {
		return "ElementAdded [elementType=" + elementType + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", id="
				+ id + "]";
	}

	public static String startsWith() {
		return "ElementAdded [";
	}

}
