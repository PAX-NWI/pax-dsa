import static org.junit.Assert.fail;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.ElementRotatedMessage;
import mocks.MockSession;

public class SessionTest {

	@Test
	public void test() throws XmppStringprepException {

		MockSession session = new MockSession();


		session.onMessageReceived(ElementRemovedMessage.class, message -> {
			System.out.println("ElementRemovedMessage = " + message);
		});
		
		session.onMessageReceived(ElementRemovedMessage.class, message -> {
			System.out.println("ElementRemovedMessage = " + message);
		});

		session.onMessageReceived(ElementAddedMessage.class, message -> {
			System.out.println("ElementAddedMessage = " + message);
		});
		
		ElementAddedMessage elementAddedMessage = new ElementAddedMessage("id", ElementType.IMAGE, 1, 2, 3, 4);
		session.simulateRecieveMessage(new Message("to",elementAddedMessage.toString()), "from");

		ElementRemovedMessage elementRemovedMessage = new ElementRemovedMessage("id");
		session.simulateRecieveMessage(new Message("to",elementRemovedMessage.toString()), "from");
		
		ElementRotatedMessage elementRotatedMessage = new ElementRotatedMessage("id",360);
		session.simulateRecieveMessage(new Message("to",elementRotatedMessage.toString()), "from");
		
		fail("Not yet implemented");
	}

}
