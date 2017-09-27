import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.stringprep.XmppStringprepException;

import de.pax.dsa.xmpp.XmppManager;

/**
 * Created by swinter on 11.04.2017.
 */
public class XmppManagerTest {
	private static final String AT = "@";

	static final String SERVER = "jabber.de";

	private XmppManager user1_manager;
	private XmppManager user2_manager;

	private String user1_username;
	private String user2_username;

	private String receivedMessage;

	@Before
	public void setUp() throws InterruptedException, IOException, SmackException, XMPPException {
		user1_username = System.getProperty("user1_username");
		user2_username = System.getProperty("user2_username");

		String user1_password = System.getProperty("user1_password");
		String user2_password = System.getProperty("user2_password");

		if (StringUtils.isNullOrEmpty(user1_username))
			fail("user1_username is empty");
		if (StringUtils.isNullOrEmpty(user2_username))
			fail("user2_username is empty");
		if (StringUtils.isNullOrEmpty(user1_password))
			fail("user1_password is empty");
		if (StringUtils.isNullOrEmpty(user2_password))
			fail("user2_password is empty");

		user1_manager = new XmppManager(SERVER, user1_username, user1_password);
		user2_manager = new XmppManager(SERVER, user2_username, user2_password);
	}

	@Test
	public void sendMessage()
			throws InterruptedException, XmppStringprepException, SmackException.NotConnectedException, XMPPException {

		user2_manager.getChatManager().addIncomingListener(new IncomingChatMessageListener() {

			@Override
			public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
				receivedMessage = message.getBody();
				System.out.println(message.getBody());
			}
		});

		user1_manager.sendMessage("test", user2_username);

		Thread.sleep(5000);

		assertEquals("test", receivedMessage);
	}

}
