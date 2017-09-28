package de.pax.dsa.model;

import java.util.ArrayList;
import java.util.List;

import de.pax.dsa.model.messages.PositionUpdatedMessage;

/**
 * not used at the moment
 * @author alexander.bunkowski
 *
 */
public class Turn {

	private List<PositionUpdatedMessage> positionUpdatedMessages;

	public Turn() {
		positionUpdatedMessages = new ArrayList<>();
	}

	public List<PositionUpdatedMessage> getPositionUpdates() {
		return positionUpdatedMessages;
	}

	public void setPositionUpdates(List<PositionUpdatedMessage> positionUpdatedMessages) {
		this.positionUpdatedMessages = positionUpdatedMessages;
	}

}
