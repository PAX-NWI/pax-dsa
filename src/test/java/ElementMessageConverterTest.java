import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.model.messages.ElementAddedMessage;
import de.pax.dsa.ui.internal.nodes.MoveableCircle;

public class ElementMessageConverterTest {

	@Test
	public void testCircleToMessage() {

		MoveableCircle circle = new MoveableCircle("id", 2, 3.0, 4);

		ElementAddedMessage message = new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(), circle.getY(), circle.getRadius(),
		circle.getRadius());

		assertEquals(circle.getId(), message.getId());
		assertEquals(ElementType.CIRCLE, message.getElementType());
		assertEquals(circle.getX(), message.getX(), 0);
		assertEquals(circle.getY(), message.getY(), 0);
		assertEquals(circle.getRadius(), message.getW(), 0);
		assertEquals(circle.getRadius(), message.getH(), 0);
	}

	@Test
	public void testMessageToCircle() {

		ElementAddedMessage message = new ElementAddedMessage("id", ElementType.CIRCLE, 2, 3.0, 4, 4);

		MoveableCircle circle = new MoveableCircle(message.getId(), message.getX(), message.getY(), message.getW());

		assertEquals(message.getId(), circle.getId());
		assertEquals(message.getX(), circle.getX(), 0);
		assertEquals(message.getY(), circle.getY(), 0);
		assertEquals(message.getW(), circle.getRadius(), 0);
		assertEquals(message.getH(), circle.getRadius(), 0);
	}

}
