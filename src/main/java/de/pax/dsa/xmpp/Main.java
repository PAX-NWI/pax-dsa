package de.pax.dsa.xmpp;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws XMPPException, InterruptedException {
        Logger logger = LoggerFactory.getLogger(Main.class);
		String server = "jabber.de";

        try {
            XmppManager manager = new XmppManager(server, args[0], args[1]);
            manager.sendMessage("test", "pax1@jabber.de");
        } catch (IOException | SmackException e) {
            logger.error("Error!", e);
        }
    }
}
