import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.pax.dsa.model.messages.PositionUpdatedMessage;

public class StringConverterTest {

	@Test
	public void testPositionUpdateTransform() {

		PositionUpdatedMessage original = new PositionUpdatedMessage("id", 52, 42);

		String positionString = original.toString();

		PositionUpdatedMessage parsed = new PositionUpdatedMessage(positionString);

		assertEquals(original.getId(), parsed.getId());
		assertEquals(original.getX(), parsed.getX(), 0);
		assertEquals(original.getY(), parsed.getY(), 0);

	}

}
