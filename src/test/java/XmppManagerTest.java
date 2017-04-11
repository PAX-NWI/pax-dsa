import de.pax.dsa.xmpp.XmppManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.junit.Before;
import org.junit.Test;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

/**
 * Created by swinter on 11.04.2017.
 */
public class XmppManagerTest {
    static final String SERVER = "jabber.de";

    private XmppManager user1_manager;
    private XmppManager user2_manager;

    private String user1_username;
    private String user2_username;

    @Before
    public void setUp() throws InterruptedException, IOException, SmackException, XMPPException {
        user1_username = System.getProperty("user1_username");
        user2_username = System.getProperty("user2_username");

        user1_manager = new XmppManager(SERVER, user1_username, System.getProperty("user1_password"));
        user2_manager = new XmppManager(SERVER, user2_username, System.getProperty("user2_password"));
    }

    @Test
    public void sendMessage() throws InterruptedException, XmppStringprepException, SmackException.NotConnectedException, XMPPException {
        user1_manager.sendMessage("test", user2_username);
    }
}
