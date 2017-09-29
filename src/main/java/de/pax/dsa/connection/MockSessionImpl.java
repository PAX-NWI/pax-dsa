package de.pax.dsa.connection;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;
import javafx.application.Platform;

public class MockSessionImpl implements IIcarusSession {

	@Inject
	private Logger logger;
	private String user;
	private Consumer<ElementAddedMessage> elementAddedConsumer;
	private Consumer<PositionUpdatedMessage> positionUpdateConsumer;

	public MockSessionImpl() {

//		new Thread() {
//			@Override
//			public void run() {
//				Platform.runLater(() -> {
//					sleep();
//					elementAddedConsumer
//							.accept(new ElementAddedMessage("test-id-1337", ElementType.CIRCLE, 100, 100, 20, 20));
//					sleep();
//					positionUpdateConsumer.accept(new PositionUpdatedMessage("test-id-1337", 50, 50));
//					sleep();
//					positionUpdateConsumer.accept(new PositionUpdatedMessage("test-id-1337", 150, 150));
//				});
//			}
//
//			private void sleep() {
//				try {
//					sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}.start();

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
	public void sendPositionUpdate(PositionUpdatedMessage positionUpdatedMessage) {
		// think about using one send method for all messages
		logger.info("Sending: {}", positionUpdatedMessage.toString());
	}

	@Override
	public void sendElementAdded(ElementAddedMessage elementAddedMessage) {
		logger.info("Sending: {}", elementAddedMessage.toString());
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

}
