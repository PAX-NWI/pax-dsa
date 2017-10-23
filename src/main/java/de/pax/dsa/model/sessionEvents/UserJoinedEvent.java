package de.pax.dsa.model.sessionEvents;

public class UserJoinedEvent implements ISessionEvent{
	
	private String name;

	public UserJoinedEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
