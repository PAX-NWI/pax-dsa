package de.pax.dsa.model.sessionEvents;

import java.io.File;

public class FileReceivedEvent implements ISessionEvent {

	private File file;

	public FileReceivedEvent(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

}
