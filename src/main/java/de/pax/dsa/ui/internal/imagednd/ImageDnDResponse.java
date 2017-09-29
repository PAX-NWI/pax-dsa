package de.pax.dsa.ui.internal.imagednd;

import javafx.scene.image.Image;

public class ImageDnDResponse {

	private Image image;
	private String name;
	private double x;
	private double y;

	public ImageDnDResponse(Image image, String name, double x, double y) {
		this.image = image;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public Image getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
