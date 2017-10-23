package de.pax.dsa.model.sessionEvents;

public class SessionConnectedEvent implements ISessionEvent {

	boolean connected;

	public SessionConnectedEvent(boolean connected) {
		this.connected = connected;
	}

	public boolean isConnected() {
		return connected;
	}

}
