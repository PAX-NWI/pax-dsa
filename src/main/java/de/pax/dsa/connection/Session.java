package de.pax.dsa.connection;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Session {
	
	private Logger logger = LoggerFactory.getLogger(Session.class);

	private Consumer<PositionUpdate> entityChangeConsumer = e -> {
		logger.warn("No consumer registered");
	};

	public void connect(String user, String password) {
		// TODO Auto-generated method stub
	}

	public void onPositionUpdate(Consumer<PositionUpdate> positionUpdateConsumer) {
		this.entityChangeConsumer = positionUpdateConsumer;
	}

	public void sendPositionUpdate(PositionUpdate positionUpdate) {
		entityChangeConsumer.accept(positionUpdate);
	}

}
