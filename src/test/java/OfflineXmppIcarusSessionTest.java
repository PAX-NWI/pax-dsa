import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.jivesoftware.smack.packet.Message;
import org.junit.Before;
import org.junit.Test;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.di.Context;
import de.pax.dsa.di.IUiSynchronize;
import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.TextMessage;
import de.pax.dsa.model.sessionEvents.ISessionEvent;
import de.pax.dsa.model.sessionEvents.UserJoinedEvent;
import de.pax.dsa.model.sessionEvents.UserLeftEvent;
import de.pax.dsa.xmpp.XmppIcarusSession;
import mocks.TestLogger;
import mocks.Wrapper;

public class OfflineXmppIcarusSessionTest {

	private XmppIcarusSession session;
	private TestLogger logger;

	@Before
	public void before() throws Exception {
		Context context = new Context();
		context.set(IUiSynchronize.class, runnable -> runnable.run());
		logger = new TestLogger();
		context.set(Logger.class, logger);
		session = context.create(XmppIcarusSession.class);
	}

	@Test
	public void testWarnOnTwoSameHandler() {
		Class<ElementRemovedMessage> messageClass = ElementRemovedMessage.class;
		session.onMessageReceived(messageClass, message -> {
			// test add first
		});

		session.onMessageReceived(messageClass, message -> {
			// test add second
		});

		assertEquals(1, logger.getWarningList().size());
		String warn = "Consumer for class " + messageClass + " already registered and will be overwritten!";
		assertEquals(warn, logger.getWarningList().get(0));
	}

	@Test
	public void testCorrectlyReceivedMessages() throws Exception {

		Wrapper<ElementAddedMessage> addedMessageReceived = new Wrapper<>();
		session.onMessageReceived(ElementAddedMessage.class, message -> {
			addedMessageReceived.value = message;
		});

		Wrapper<ElementRemovedMessage> removedMessageReceived = new Wrapper<>();
		session.onMessageReceived(ElementRemovedMessage.class, message -> {
			removedMessageReceived.value = message;
		});

		ElementAddedMessage elementAddedMessage = new ElementAddedMessage("id", ElementType.IMAGE, 1, 2, 3, 4);
		simulateProcessMessage(session, stringToTestMessage(elementAddedMessage.toString()));

		ElementRemovedMessage elementRemovedMessage = new ElementRemovedMessage("id");
		simulateProcessMessage(session, stringToTestMessage(elementRemovedMessage.toString()));

		assertEquals(0, logger.getWarningList().size());
		assertEquals(elementAddedMessage.toString(), addedMessageReceived.value.toString());
		assertEquals(elementRemovedMessage.toString(), removedMessageReceived.value.toString());
	}

	@Test
	public void testSpecialCharacterInTextMessage1() throws Exception {
		testMessage(new TextMessage(" id seperator --- used in text, and commas , "));
	}

	@Test
	public void testSpecialCharacterInTextMessage2() throws Exception {
		testMessage(new TextMessage("[bracets]  [in text]"));
	}

	@Test
	public void testSpecialCharacterInTextMessage3() throws Exception {
		testMessage(new TextMessage("equal = signs = inside"));
	}

	@Test
	public void testSpecialCharacterInTextMessage4() throws Exception {
		testMessage(new TextMessage("\"quots around \" different \"blocks inside\" "));
	}

	@Test
	public void testSpecialCharacterInTextMessage5() throws Exception {
		testMessage(new TextMessage("quote in \" the middle"));
	}

	@Test
	public void testSpecialCharacterInTextMessageWild() throws Exception {
		testMessage(new TextMessage("�^�$%&/()=?��{[]};,.:<>|=?*+~'#_-\"\"\"\"\\"));
	}

	private void testMessage(TextMessage textMessage) throws Exception, XmppStringprepException {
		Wrapper<TextMessage> textMessageReceived = new Wrapper<>();
		session.onMessageReceived(TextMessage.class, message -> {
			textMessageReceived.value = message;
		});

		simulateProcessMessage(session, stringToTestMessage(textMessage.toString()));

		assertEquals(0, logger.getWarningList().size());
		assertEquals(textMessage.toString(), textMessageReceived.value.toString());
	}

	@Test
	public void testWarnMessageRecievedWithoutHandler() throws Exception {
		ElementAddedMessage elementAddedMessage = new ElementAddedMessage("id", ElementType.IMAGE, 1, 2, 3, 4);
		Message addedMessageSent = stringToTestMessage(elementAddedMessage.toString());
		simulateProcessMessage(session, addedMessageSent);

		assertEquals(1, logger.getWarningList().size());
		String warn = "No Consumer registered for " + ElementAddedMessage.class;
		assertEquals(warn, logger.getWarningList().get(0));

	}

	@Test
	public void testEvents() throws Exception {
		UserJoinedEvent userJoinedEvent = new UserJoinedEvent("username");
		Wrapper<UserJoinedEvent> userJoinedEventReceived = new Wrapper<>();
		session.onSessionEvent(UserJoinedEvent.class, e -> {
			userJoinedEventReceived.value = e;
		});

		UserLeftEvent userLeftEvent = new UserLeftEvent("username");
		Wrapper<UserLeftEvent> userLeftEventReceived = new Wrapper<>();
		session.onSessionEvent(UserLeftEvent.class, e -> {
			userLeftEventReceived.value = e;
		});
		
		simulateProcessEvent(session, userJoinedEvent);
		simulateProcessEvent(session, userLeftEvent);
		
		assertEquals(0, logger.getWarningList().size());
		assertEquals(userJoinedEvent, userJoinedEventReceived.value);
		assertEquals(userLeftEvent, userLeftEventReceived.value);
	}

	private Message stringToTestMessage(String text) throws XmppStringprepException {
		Message addedMessageSent = new Message("to", text);
		addedMessageSent.setFrom(JidCreate.from("from@test.de/unitTest"));
		return addedMessageSent;
	}

	/**
	 * Using reflection to test the private method "processMessage" without
	 * connecting to an actual xmpp server
	 */
	private void simulateProcessMessage(IIcarusSession session, Message message) throws Exception {
		Method declaredMethod = XmppIcarusSession.class.getDeclaredMethod("processMessage", Message.class);
		declaredMethod.setAccessible(true);
		declaredMethod.invoke(session, message);
	}

	/**
	 * Using reflection to test the private method "processEvent" without
	 * connecting to an actual xmpp server
	 */
	private void simulateProcessEvent(IIcarusSession session, ISessionEvent message) throws Exception {
		Method declaredMethod = XmppIcarusSession.class.getDeclaredMethod("processSessionEvent", ISessionEvent.class);
		declaredMethod.setAccessible(true);
		declaredMethod.invoke(session, message);
	}

}
