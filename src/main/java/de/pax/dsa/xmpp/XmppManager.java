package de.pax.dsa.xmpp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
import org.jivesoftware.smackx.muc.DefaultParticipantStatusListener;
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

import de.pax.dsa.di.IUiSynchronize;
import de.pax.dsa.model.sessionEvents.FileReceivedEvent;
import de.pax.dsa.model.sessionEvents.ISessionEvent;
import de.pax.dsa.model.sessionEvents.UserJoinedEvent;
import de.pax.dsa.model.sessionEvents.UserLeftEvent;

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

	private Roster roster;

	public XmppManager(String server, String username, String password, IUiSynchronize uiSynchronize)
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

		roster = Roster.getInstanceFor(connection);
		roster.setSubscriptionMode(SubscriptionMode.accept_all);

		EntityBareJid room = JidCreate.entityBareFrom(ROOM_NAME + ROOM_PROVIDER);
		MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
		multiUserChat = manager.getMultiUserChat(room);

		MucEnterConfiguration.Builder enterConfig = multiUserChat
				.getEnterConfigurationBuilder(Resourcepart.from(username));
		enterConfig.requestNoHistory();

		multiUserChat.join(enterConfig.build());

		chatManager = ChatManager.getInstanceFor(connection);
	}

	private void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public List<String> getAllOtherUsers() {

		List<EntityFullJid> participants = multiUserChat.getOccupants();

		return participants.stream()//
				.filter(p -> !p.getResourceOrThrow().equals(multiUserChat.getNickname()))//
				.map(p -> String.valueOf(p.getResourceOrThrow()))//
				.collect(Collectors.toList());

	}

	protected boolean shouldAccept(FileTransferRequest request) {
		String filename = request.getFileName().toLowerCase();
		return (filename.endsWith(".png") || filename.endsWith(".jpeg") || filename.endsWith(".jpg"));
	}

	public void disconnect() {
		if (connection != null) {
			connection.disconnect();
		}
	}

	public void addMessageListener(MessageListener messageListener) {
		multiUserChat.addMessageListener(messageListener);
		chatManager.addIncomingListener((from, message, chat) -> messageListener.processMessage(message));
	}

	public <T> void addSessionEventListener(Consumer<ISessionEvent> sessionEventConsumer) {
		multiUserChat.addParticipantStatusListener(new DefaultParticipantStatusListener() {
			@Override
			public void joined(EntityFullJid participant) {
				logger.debug("New User entered: " + participant.getResourcepart());
				sessionEventConsumer.accept(new UserJoinedEvent(String.valueOf(participant.getResourcepart())));
			}

			@Override
			public void left(EntityFullJid participant) {
				logger.debug("User left: " + participant.getResourcepart());
				sessionEventConsumer.accept(new UserLeftEvent(String.valueOf(participant.getResourcepart())));
			}
		});

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
							logger.debug(transfer.getStatus() + " - " + transfer.getProgress());
						}
						sleep(1000);
					}
					sessionEventConsumer.accept(new FileReceivedEvent(file));

				} else {
					logger.warn("Rejecting file");
					request.reject();
				}
			} catch (SmackException | IOException | InterruptedException e) {
				logger.error("Error receiving file", e);
			}
		});

	}

	public void sendMessage(String message) {
		logger.debug("Sending mesage '{}' to chat.", message);
		try {
			multiUserChat.sendMessage(message);
		} catch (NotConnectedException | InterruptedException e) {
			logger.error("Can not send Message to Chat", e);
		}
	}

	public boolean sendMessage(String message, String buddyJID) {
		logger.debug("Sending mesage '{}' to user {}", message, buddyJID);

		EntityBareJid jid = null;
		try {
			jid = JidCreate.entityBareFrom(buddyJID + "@" + server);
		} catch (XmppStringprepException e) {
			logger.error("Can not send message to user " + buddyJID + ", username may contain illegal chars.", e);
			return false;
		}

		if (!roster.getPresence(jid).isAvailable()) {
			logger.warn("User {} is offline, can not send Message.", jid);
			return false;
		}

		Chat chat = chatManager.chatWith(jid);
		try {
			chat.send(message);
			return true;
		} catch (NotConnectedException | InterruptedException e) {
			logger.error("Error sending message to user " + jid, e);
			return false;
		}
	}

	public void sendFile(String buddyJID, File file) {
		logger.debug("Sending file '{}' to user {}", file.getName(), buddyJID);
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

}
