import static org.junit.Assert.assertEquals;

import org.jivesoftware.smack.packet.Message;
import org.junit.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.ElementRemovedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;

public class StringMapperTest {

	@Test
	public void testPositionUpdateTransform() throws XmppStringprepException {

		PositionUpdatedMessage original = new PositionUpdatedMessage("id", 52, 42);
		
		Message message = new Message("to", original.toString());
		PositionUpdatedMessage parsed = new PositionUpdatedMessage(message,"sender");

		assertEquals(original.getId(), parsed.getId());
		assertEquals(original.getX(), parsed.getX(), 0);
		assertEquals(original.getY(), parsed.getY(), 0);
	}
	
	@Test
	public void testElementAddedMessageTransform() throws XmppStringprepException {

		ElementAddedMessage original = new ElementAddedMessage("id", ElementType.CIRCLE, 2, 3.0, 4, 5);
	
		Message message = new Message("to", original.toString());
		ElementAddedMessage parsed = new ElementAddedMessage(message,"sender");

		assertEquals(original.getId(), parsed.getId());
		assertEquals(original.getElementType(), parsed.getElementType());
		assertEquals(original.getX(), parsed.getX(), 0);
		assertEquals(original.getY(), parsed.getY(), 0);
		assertEquals(original.getW(), parsed.getW(), 0);
		assertEquals(original.getH(), parsed.getH(), 0);
	}
	
	@Test
	public void testElementRemovedMessageTransform() throws XmppStringprepException {

		ElementRemovedMessage original = new ElementRemovedMessage("test-id-1234");
	
		Message message = new Message("to", original.toString());
		ElementRemovedMessage parsed = new ElementRemovedMessage(message,"sender");

		assertEquals(original.getId(), parsed.getId());

	}

}
