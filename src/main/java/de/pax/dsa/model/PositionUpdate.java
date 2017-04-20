package de.pax.dsa.model;

public class PositionUpdate {

	@Override
	public String toString() {
		return "PositionUpdate [id=" + id + ", x=" + x + ", y=" + y + "]";
	}

	private String id;
	private double x;
	private double y;

	public PositionUpdate(String id, double x, double y) {
		this.id = id;
		this.x = x;
		this.y = y;
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

}
