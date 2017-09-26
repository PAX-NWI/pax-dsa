package de.pax.dsa.mapreveal.moveables;

import de.pax.dsa.ui.internal.dragsupport.I2DObject;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RevealRectangle extends Rectangle implements I2DObject {

	public enum State {
		KNOWN, UNKNOW, CURRENT
	}

	private State state = State.UNKNOW;

	public RevealRectangle(int i, int j) {
		super(i, j);
		setStroke(Color.DARKGRAY);
		setFill(Color.GREY);
		update();
	}

	public void reveal() {
		if (state != State.CURRENT) {
			state = State.CURRENT;
			update();
		}
	}

	public void unReveal() {
		if (state == State.CURRENT) {
			state = State.KNOWN;
			update();
		}
	}

	private void update() {
		switch (state) {
		case UNKNOW:
			setOpacity(1);
			break;
		case CURRENT:
			setOpacity(0);
			break;
		case KNOWN:
			setOpacity(0.5);
			break;
		}
	}

}
