import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.pax.dsa.model.PositionUpdate;

public class StringConverterTest {

	@Test
	public void testPositionUpdateTransform() {

		PositionUpdate original = new PositionUpdate("id", 52, 42);

		String positionString = original.toString();

		PositionUpdate parsed = new PositionUpdate(positionString);

		assertEquals(original.getId(), parsed.getId());
		assertEquals(original.getX(), parsed.getX(), 0);
		assertEquals(original.getY(), parsed.getY(), 0);

	}

}
