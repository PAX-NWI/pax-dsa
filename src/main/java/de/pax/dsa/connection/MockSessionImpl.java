package de.pax.dsa.connection;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.di.Context;
import de.pax.dsa.model.PositionUpdate;

public class MockSessionImpl implements IIcarusSession{
	
	@Inject
	private Context context;
	
	private Logger logger = LoggerFactory.getLogger(MockSessionImpl.class);

	private Consumer<PositionUpdate> entityChangeConsumer = e -> {
		logger.warn("No consumer registered");
	};
	
	@Override
	public void connect(String user, String password) {
		logger.info("Connected User {}",user);
	}

	@Override
	public void onPositionUpdate(Consumer<PositionUpdate> positionUpdateConsumer) {
		//this.entityChangeConsumer = positionUpdateConsumer;
	}
	
	@Override
	public void sendPositionUpdate(PositionUpdate positionUpdate) {
		logger.info(positionUpdate.toString());
	//	entityChangeConsumer.accept(positionUpdate);
	}

}
