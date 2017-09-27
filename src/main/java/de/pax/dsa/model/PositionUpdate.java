package de.pax.dsa.model;

import java.util.Map;

public class PositionUpdate {

	private String id;
	private double x;
	private double y;

	public PositionUpdate(String id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	public PositionUpdate(String string) {
		Map<String, String> map = StringMapper.keyValueListStringToMap(string);
		setId(map.get("id"));
		setX(Double.parseDouble(map.get("x")));
		setY(Double.parseDouble(map.get("y")));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "PositionUpdate [id=" + id + ", x=" + x + ", y=" + y + "]";
	}
}
