package de.pax.dsa.connection;

import java.util.function.Consumer;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;

public interface IIcarusSession {

	void connect(String user, String password);

	void onPositionUpdate(Consumer<PositionUpdatedMessage> positionUpdateConsumer);

	void sendPositionUpdate(PositionUpdatedMessage positionUpdatedMessage);

	void sendElementAdded(ElementAddedMessage elementAddedMessage);

	void onElementAdded(Consumer<ElementAddedMessage> positionUpdateConsumer);

}
