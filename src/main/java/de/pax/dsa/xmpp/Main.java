package de.pax.dsa.xmpp;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class Main {
	
	public static void main(String[] args) throws XMPPException, InterruptedException {
		String server = "jabber.de";
		int port = 5222;
	
		XmppManager xmppManager = new XmppManager(server, port);
		
		
		xmppManager.init();
		
		xmppManager.performLogin("pax2", "paxnwi");
		
		
	
		
		
		xmppManager.sendMessage("test", "pax1@jabber.de");
		
		
		xmppManager.destroy();
	}

}
