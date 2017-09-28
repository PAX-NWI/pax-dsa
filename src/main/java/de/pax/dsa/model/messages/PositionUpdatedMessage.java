package de.pax.dsa.model.messages;

import java.util.Map;

import de.pax.dsa.model.StringMapper;

public class PositionUpdatedMessage {

	private String id;
	private double x;
	private double y;

	public PositionUpdatedMessage(String id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public PositionUpdatedMessage(String string) {
		Map<String, String> map = StringMapper.keyValueListStringToMap(string);
		this.id = map.get("id");
		this.x = Double.parseDouble(map.get("x"));
		this.y = Double.parseDouble(map.get("y"));
	}

	public String getId() {
		return id;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public String toString() {
		return "PositionUpdate [id=" + id + ", x=" + x + ", y=" + y + "]";
	}
}
