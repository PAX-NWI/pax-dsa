package de.pax.dsa.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {

	private List<PositionUpdate> positionUpdates;

	public Turn() {
		positionUpdates = new ArrayList<>();
	}

	public List<PositionUpdate> getPositionUpdates() {
		return positionUpdates;
	}

	public void setPositionUpdates(List<PositionUpdate> positionUpdates) {
		this.positionUpdates = positionUpdates;
	}

}
