import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.model.messages.PositionUpdatedMessage;

public class StringMapperTest {

	@Test
	public void testPositionUpdateTransform() {

		PositionUpdatedMessage original = new PositionUpdatedMessage("id", 52, 42);
		String positionString = original.toString();

		PositionUpdatedMessage parsed = new PositionUpdatedMessage(positionString);

		assertEquals(original.getId(), parsed.getId());
		assertEquals(original.getX(), parsed.getX(), 0);
		assertEquals(original.getY(), parsed.getY(), 0);
	}
	
	@Test
	public void testElementAddedMessageTransform() {

		ElementAddedMessage original = new ElementAddedMessage("id", ElementType.CIRCLE, 2, 3.0, 4, 5);
		String positionString = original.toString();
		
		ElementAddedMessage parsed = new ElementAddedMessage(positionString);

		assertEquals(original.getId(), parsed.getId());
		assertEquals(original.getElementType(), parsed.getElementType());
		assertEquals(original.getX(), parsed.getX(), 0);
		assertEquals(original.getY(), parsed.getY(), 0);
		assertEquals(original.getW(), parsed.getW(), 0);
		assertEquals(original.getH(), parsed.getH(), 0);
	}

}
