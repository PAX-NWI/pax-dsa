package de.pax.dsa.model;

public class Character {

	private String name;
	private double xPosition;
	private double yPosition;

	public Character(String name, double xPosition, double yPosition) {
		this.name = name;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getxPosition() {
		return xPosition;
	}

	public void setxPosition(double xPosition) {
		this.xPosition = xPosition;
	}

	public double getyPosition() {
		return yPosition;
	}

	public void setyPosition(double yPosition) {
		this.yPosition = yPosition;
	}

}
