package de.pax.dsa.model.messages;

import java.util.Map;

import de.pax.dsa.model.StringMapper;

public class RequestFileMessage {

	private String owner;
	private String fileName;
	private String requester;

	public RequestFileMessage(String owner, String fileName, String requester) {
		this.owner = owner;
		this.fileName = fileName;
		this.requester = requester;
	}

	public RequestFileMessage(String message) {
		Map<String, String> map = StringMapper.keyValueListStringToMap(message);
		this.owner = map.get("owner");
		this.fileName = map.get("fileName");
		this.requester = map.get("requester");
	}

	public String getOwner() {
		return owner;
	}

	public String getFileName() {
		return fileName;
	}
	
	public String getRequester() {
		return requester;
	}

	@Override
	public String toString() {
		return "RequestFile [owner=" + owner + ", fileName=" + fileName + ", requester=" + requester + "]";
	}

	public static String startsWith() {
		return "RequestFile [";
	}

}
