package de.pax.dsa.connection;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;

public class MockSessionImpl implements IIcarusSession{
	
	@Inject
	private Logger logger;
	
	@Override
	public void connect(String user, String password) {
		logger.info("Connected User {}",user);
	}

	@Override
	public void onPositionUpdate(Consumer<PositionUpdatedMessage> positionUpdateConsumer) {
		
	}
	
	@Override
	public void sendPositionUpdate(PositionUpdatedMessage positionUpdatedMessage) {
		logger.info("Sending: {}",positionUpdatedMessage.toString());
	}

	@Override
	public void sendElementAdded(ElementAddedMessage elementAddedMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onElementAdded(Consumer<ElementAddedMessage> positionUpdateConsumer) {
		// TODO Auto-generated method stub
		
	}

}
