package de.pax.dsa.model.messages;

import java.util.Map;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.StringMapper;

public class ElementAddedMessage {

	private ElementType elementType;
	private double x;
	private double y;
	private double w;
	private double h;
	private String id;

	public ElementAddedMessage(String id, ElementType circle, double x, double y, double w, double h) {
		this.id = id;
		this.elementType = circle;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public ElementAddedMessage(String  string) {
		Map<String, String> map = StringMapper.keyValueListStringToMap(string);
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
		return "ElementAddedMessage [elementType=" + elementType + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h
				+ ", id=" + id + "]";
	}



}
