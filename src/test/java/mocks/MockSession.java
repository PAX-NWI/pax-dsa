package mocks;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jivesoftware.smack.packet.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.MessageConverter;
import de.pax.dsa.model.messages.IMessage;

public class MockSession implements IIcarusSession {

	private static Logger logger = LoggerFactory.getLogger(MockSession.class);

	Map<Class<?>, Consumer<?>> messageConsumerList = new HashMap<>();


	@Override
	public <T> void onMessageReceived(Class<T> messageClass, Consumer<T> consumer) {
		if (messageConsumerList.containsKey(messageClass)) {
			logger.warn("Consumer for class {} already registered and will be overwritten!", messageClass);
		}
		messageConsumerList.put(messageClass, consumer);
		
	}
	public void simulateRecieveMessage(Message message, String sender) {
	//	Platform.runLater(() -> {
			logger.info("Received message:" + message.getBody());
			Object decode = MessageConverter.decode(message, sender.toString());
			Consumer consumer = messageConsumerList.get(decode.getClass());
			if (consumer != null) {
				consumer.accept(decode);
			} else {
				logger.warn("No Consumer registered for {}", decode.getClass());
			}

	//	});
	}

	@Override
	public void connect(String user, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServer() {
		// TODO Auto-generated method stub
		return null;
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
	public void onUserEntered(Consumer<String> onUserEnteredConsumer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessageToUser(IMessage message, String name) {
		// TODO Auto-generated method stub
		
	}



}
