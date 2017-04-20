package de.pax.dsa.connection;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.model.PositionUpdate;

public class MockSessionImpl implements IIcarusSession{
	
	private Logger logger = LoggerFactory.getLogger(MockSessionImpl.class);

	private Consumer<PositionUpdate> entityChangeConsumer = e -> {
		logger.warn("No consumer registered");
	};
	@Override
	public void connect(String user, String password) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPositionUpdate(Consumer<PositionUpdate> positionUpdateConsumer) {
		this.entityChangeConsumer = positionUpdateConsumer;
	}
	@Override
	public void sendPositionUpdate(PositionUpdate positionUpdate) {
		entityChangeConsumer.accept(positionUpdate);
	}

}
