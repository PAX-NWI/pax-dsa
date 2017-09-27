package de.pax.dsa.xmpp;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration.Builder;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.net.ssl.SSLSocketFactory;

/**
 * Smack Documentation:
 * https://github.com/igniterealtime/Smack/blob/master/documentation/index.md
 *
 *
 * Get Screenshot from Webcam: https://github.com/sarxos/webcam-capture
 * 
 * server reachable at 80 / 443
 * https://www.einfachjabber.de/blog/2011-03-21_firewall_sperren_umgehen.html
 */

public class XmppManager {
	private static Logger logger = LoggerFactory.getLogger(XmppManager.class);

	private AbstractXMPPConnection connection;
	private ChatManager chatManager;
	private MessageListener messageListener;

	private String server;

	public XmppManager(String server, String username, String password)
			throws XMPPException, IOException, InterruptedException, SmackException {
		this.server = server;
		logger.debug("Initializing connection to server {}", server);
		SmackConfiguration.DEBUG = true;

		Builder config = XMPPTCPConnectionConfiguration.builder();
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
		config.setSocketFactory(SSLSocketFactory.getDefault());
		config.setXmppDomain(server);
		config.setResource("IcarusClient");
		config.setUsernameAndPassword(username, password);
	//	config.setPort(443);
		config.setDebuggerEnabled(true);
		// config.setConnectTimeout(2000);

		connection = new XMPPTCPConnection(config.build());

		connection.connect();
		logger.debug("Connected: {}", connection.isConnected());

		connection.login();
		logger.debug("Authenticated: {}", connection.isAuthenticated());

		chatManager = ChatManager.getInstanceFor(connection);
	}

	public ChatManager getChatManager() {
		return chatManager;
	}

	public void sendMessage(String message, String buddyJID)
			throws XMPPException, XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
		logger.debug("Sending mesage '{}' to user {}", message, buddyJID);
		if (!buddyJID.contains("@")) {
			throw new IllegalStateException("target buddy id must contain @");
		}

		EntityBareJid jid = JidCreate.entityBareFrom(buddyJID + "@" + server);
		Chat chat = chatManager.chatWith(jid);
		chat.send(message);
	}
}
