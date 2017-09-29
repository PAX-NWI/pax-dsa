package de.pax.dsa.xmpp;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration.Builder;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Smack Documentation:
 * https://github.com/igniterealtime/Smack/blob/master/documentation/index.md
 *
 *
 * Get Screenshot from Webcam: https://github.com/sarxos/webcam-capture
 * 
 * server reachable at 80 / 443
 * https://www.einfachjabber.de/blog/2011-03-21_firewall_sperren_umgehen.html
 * 
 * Multi User Chat
 * https://github.com/igniterealtime/Smack/blob/master/documentation/extensions/muc.md
 */

public class XmppManager {
	private static final String ROOM_PROVIDER = "@conference.jabber.de";

	private static final String ROOM_NAME = "icarus43";

	private static Logger logger = LoggerFactory.getLogger(XmppManager.class);

	private AbstractXMPPConnection connection;
	private ChatManager chatManager;

	private String server;

	private MultiUserChat multiUserChat;

	public XmppManager(String server, String username, String password)
			throws XMPPException, IOException, InterruptedException, SmackException {
		this.server = server;
		logger.debug("Initializing connection to server {}", server);
	//	SmackConfiguration.DEBUG = true;

		XMPPTCPConnectionConfiguration.Builder connectionConfig = XMPPTCPConnectionConfiguration.builder();
		connectionConfig.setXmppDomain(server);
		connectionConfig.setResource("IcarusClient");
		connectionConfig.setUsernameAndPassword(username, password);
		//connectionConfig.setDebuggerEnabled(true);

		connection = new XMPPTCPConnection(connectionConfig.build());

		connection.connect();
		logger.debug("Connected: {}", connection.isConnected());

		connection.login();
		logger.debug("Authenticated: {}", connection.isAuthenticated());

		EntityBareJid room = JidCreate.entityBareFrom(ROOM_NAME + ROOM_PROVIDER);
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
		multiUserChat = manager.getMultiUserChat(room);

		MucEnterConfiguration.Builder enterConfig = multiUserChat
				.getEnterConfigurationBuilder(Resourcepart.from(username));
		enterConfig.requestNoHistory();

		multiUserChat.join(enterConfig.build());

		chatManager = ChatManager.getInstanceFor(connection);
	}

	public void disconnect() {
		if (connection != null) {
			connection.disconnect();
		}
	}

	public void addMessageListener(MessageListener messageListener) {
		multiUserChat.addMessageListener(messageListener);
	}

	/**
	 * Use MultiUserChat instead
	 */
	@Deprecated
	public void sendMessage(String message, String buddyJID)
			throws XMPPException, XmppStringprepException, SmackException.NotConnectedException, InterruptedException {
		logger.debug("Sending mesage '{}' to user {}", message, buddyJID);

		EntityBareJid jid = JidCreate.entityBareFrom(buddyJID + "@" + server);
		Chat chat = chatManager.chatWith(jid);
		chat.send(message);
	}

	public void sendMessage(String message) {
		try {
			multiUserChat.sendMessage(message);
		} catch (NotConnectedException | InterruptedException e) {
			logger.error("Can not send Message to Chat", e);
		}

	}
}
