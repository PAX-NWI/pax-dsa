package de.pax.dsa.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;

/**
 * Smack Documentation:
 * https://github.com/igniterealtime/Smack/blob/master/documentation/index.md
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
	public static final String FOLDER = "images/";

	private static final String ROOM_PROVIDER = "@conference.jabber.de";

	private static final String ROOM_NAME = "icarus43";

	private static Logger logger = LoggerFactory.getLogger(XmppManager.class);

	private AbstractXMPPConnection connection;
	private ChatManager chatManager;

	private String server;

	private MultiUserChat multiUserChat;

	private FileTransferManager fileTransferManager;

	private Consumer<File> onFileReceivedConsumer;

	public XmppManager(String server, String username, String password)
			throws XMPPException, IOException, InterruptedException, SmackException {
		this.server = server;
		logger.debug("Initializing connection to server {}", server);
		// SmackConfiguration.DEBUG = true;

		XMPPTCPConnectionConfiguration.Builder connectionConfig = XMPPTCPConnectionConfiguration.builder();
		connectionConfig.setXmppDomain(server);
		connectionConfig.setResource("IcarusClient");
		connectionConfig.setUsernameAndPassword(username, password);
		// connectionConfig.setDebuggerEnabled(true);

		connection = new XMPPTCPConnection(connectionConfig.build());

		connection.connect();
		logger.debug("Connected: {}", connection.isConnected());

		connection.login();
		logger.debug("Authenticated: {}", connection.isAuthenticated());

		Roster roster = Roster.getInstanceFor(connection);
		roster.setSubscriptionMode(SubscriptionMode.accept_all);

		EntityBareJid room = JidCreate.entityBareFrom(ROOM_NAME + ROOM_PROVIDER);
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
		multiUserChat = manager.getMultiUserChat(room);

		MucEnterConfiguration.Builder enterConfig = multiUserChat
				.getEnterConfigurationBuilder(Resourcepart.from(username));
		enterConfig.requestNoHistory();

		multiUserChat.join(enterConfig.build());

		fileTransferManager = FileTransferManager.getInstanceFor(connection);

		fileTransferManager.addFileTransferListener(request -> {
			try {
				if (shouldAccept(request)) {
					IncomingFileTransfer transfer = request.accept();
					File file = new File(FOLDER + transfer.getFileName());
					transfer.recieveFile(file);
					while (!transfer.isDone()) {
						if (transfer.getStatus().equals(Status.error)) {
							logger.error("Error receiving file: " + transfer.getError());
						} else {
							logger.info(transfer.getStatus() + " - " + transfer.getProgress());
						}
						sleep(1000);
					}

					Platform.runLater(() -> {
						if (onFileReceivedConsumer != null) {
							onFileReceivedConsumer.accept(file);
						}
					});

				} else {
					logger.warn("Rejecting file");
					request.reject();
				}
			} catch (SmackException | IOException | InterruptedException e) {
				logger.error("Error receiving file", e);
			}
		});

		chatManager = ChatManager.getInstanceFor(connection);
	}

	protected void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void onFileReceived(Consumer<File> onFileReceivedConsumer) {
		this.onFileReceivedConsumer = onFileReceivedConsumer;
	}

	protected boolean shouldAccept(FileTransferRequest request) {
		String filename = request.getFileName();
		if (filename.endsWith(".png") || filename.endsWith(".jpeg") || filename.endsWith(".jpg")) {
			return true;
		}
		return false;
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

	public void sendFile(String buddyJID, File file) {
		EntityFullJid jid = null;
		try {
			jid = JidCreate.entityFullFrom(buddyJID + "@" + server + "/IcarusClient");
		} catch (XmppStringprepException e) {
			logger.error("Can not send File, error creating target user id", e);
		}
		OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(jid);
		try {
			transfer.sendFile(file, "Info");
		} catch (SmackException e) {
			logger.error("Can not send file", e);
		}

	}

	public void sendMessage(String message) {
		try {
			multiUserChat.sendMessage(message);
		} catch (NotConnectedException | InterruptedException e) {
			logger.error("Can not send Message to Chat", e);
		}

	}
}
