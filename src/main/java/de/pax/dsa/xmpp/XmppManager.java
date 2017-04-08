package de.pax.dsa.xmpp;


import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XmppManager {
    private static Logger logger = LoggerFactory.getLogger(XmppManager.class);

    private AbstractXMPPConnection connection;
    private ChatManager chatManager;
	private MessageListener messageListener;

	public XmppManager(String server, String username, String password) throws XMPPException, IOException, InterruptedException, SmackException  {
        logger.debug("Initializing connection to server {}", server);
        SmackConfiguration.DEBUG = true;

        XMPPTCPConnectionConfiguration configuration = XMPPTCPConnectionConfiguration.builder()
                .setXmppDomain(server)
                .setResource("SomeResource")
                .setUsernameAndPassword(username, password)
                .build();

        connection = new XMPPTCPConnection(configuration);

        connection.connect();

        logger.debug("Connected: {}", connection.isConnected());

        chatManager = ChatManager.getInstanceFor(connection);
	}

	public void sendMessage(String message, String buddyJID) throws XMPPException, XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
        logger.debug("Sending mesage '{}' to user {}", message, buddyJID);
        EntityBareJid jid = JidCreate.entityBareFrom(buddyJID);
        Chat chat = chatManager.chatWith(jid);
        chat.send(message);
	}
}
