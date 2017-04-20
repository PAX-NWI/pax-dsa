package de.pax.dsa.connection;

import java.util.function.Consumer;

import de.pax.dsa.model.PositionUpdate;

public interface IIcarusSession {

	void connect(String user, String password);

	void onPositionUpdate(Consumer<PositionUpdate> positionUpdateConsumer);

	void sendPositionUpdate(PositionUpdate positionUpdate);

}
