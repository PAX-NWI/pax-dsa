package de.pax.dsa.model.messages;

import de.pax.dsa.model.ElementType;
import de.pax.dsa.ui.internal.nodes.MoveableCircle;

public class ElementMessageConverter {

	public static ElementAddedMessage createCircleAddedMessage(MoveableCircle circle) {
		return new ElementAddedMessage(circle.getId(), ElementType.CIRCLE, circle.getX(), circle.getY(), circle.getRadius(),
				circle.getRadius());
	}

	public static MoveableCircle circleFromMessage(ElementAddedMessage m) {
		return new MoveableCircle(m.getId(), m.getX(), m.getY(), m.getW());
	}


}
