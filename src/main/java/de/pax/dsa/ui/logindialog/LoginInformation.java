package de.pax.dsa.ui.logindialog;

public class LoginInformation {

	private String userName;
	private String password;

	public LoginInformation(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}
