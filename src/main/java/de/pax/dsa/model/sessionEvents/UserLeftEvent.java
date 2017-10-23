package de.pax.dsa.model.sessionEvents;

public class UserLeftEvent implements ISessionEvent {

	private String name;

	public UserLeftEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
