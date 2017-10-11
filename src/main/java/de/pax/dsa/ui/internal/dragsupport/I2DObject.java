package de.pax.dsa.ui.internal.dragsupport;

/**
 * Interface to simplify the getting and setting of position info.
 * 
 * Different Nodes require different types of coordinates to be set. (Circle =
 * getCenterX(), Node = getLayoutX(), ImageView = getX())
 * 
 * With the help of this Interface all position related stuff can be done with
 * getX/setX/getY/setY
 * 
 * @author alex
 *
 */
public interface I2DObject {

	public double getX();

	public void setX(double x);

	public double getY();

	public void setY(double y);
}
