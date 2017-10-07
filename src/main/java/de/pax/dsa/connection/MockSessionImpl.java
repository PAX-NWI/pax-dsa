package de.pax.dsa.connection;

import java.io.File;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.IMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import de.pax.dsa.model.messages.RequestFileMessage;

public class MockSessionImpl implements IIcarusSession {

	@Inject
	private Logger logger;
	private String user;
	private Consumer<ElementAddedMessage> elementAddedConsumer;
	private Consumer<PositionUpdatedMessage> positionUpdateConsumer;

	public MockSessionImpl() {
	}

	@Override
	public void connect(String user, String password) {
		this.user = user;
		logger.info("Connected User {}", user);
	}

	@Override
	public void onPositionUpdate(Consumer<PositionUpdatedMessage> positionUpdateConsumer) {
		this.positionUpdateConsumer = positionUpdateConsumer;
	}


	@Override
	public void onElementAdded(Consumer<ElementAddedMessage> elementAddedConsumer) {
		this.elementAddedConsumer = elementAddedConsumer;
	}

	@Override
	public void disconnect() {
		logger.info("Disconnected User {}", user);
	}

	@Override
	public String getUserName() {
		return user;
	}

	@Override
	public String getServer() {
		return "server.de";
	}

	@Override
	public void onRequestFile(Consumer<RequestFileMessage> onRequestFileConsumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileReceived(Consumer<File> onFileReceivedConsumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFile(String buddyJID, File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(IMessage message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onElementRemoved(Consumer<ElementRemovedMessage> onElementRemovedConsumer) {
		// TODO Auto-generated method stub
		
	}

}
