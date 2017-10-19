import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.util.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import de.pax.dsa.xmpp.XmppManager;
import mocks.MockUiSync;

/**
 * Created by swinter on 11.04.2017.
 */
public class OnlineXmppManagerTest {

	// realistic test message to check correct processing of special characters
	private static final String TEST_MESSAGE = "PositionUpdate [id=" + "C:/possible/path\\chars" + ", x=" + 10.3
			+ ", y=" + 200 + "]";

	static final String SERVER = "jabber.de";

	private XmppManager user1_manager;
	private XmppManager user2_manager;

	private String receivedMessage;

	@Before
	public void setUp() throws InterruptedException, IOException, SmackException, XMPPException {
		String user1_username = System.getProperty("user1_username");
		String user2_username = System.getProperty("user2_username");

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

		user1_manager = new XmppManager(SERVER, user1_username, user1_password, new MockUiSync());
		user2_manager = new XmppManager(SERVER, user2_username, user2_password, new MockUiSync());
	}

	@Test
	public void sendMessage()
			throws InterruptedException, XmppStringprepException, SmackException.NotConnectedException, XMPPException {

		user2_manager.addMessageListener((message) -> {
			receivedMessage = message.getBody();
		});

		user1_manager.sendMessage(TEST_MESSAGE);

		// wait some time for the response
		Thread.sleep(5000);

		assertEquals(TEST_MESSAGE, receivedMessage);
		//
		// File file = new File("src/main/resources/KibaMap.png");
		// user1_manager.sendFile("pax2", file);
		//
		// Thread.sleep(5000);
	}

	@After
	public void after() {
		user1_manager.disconnect();
		user2_manager.disconnect();
	}

}
