package de.pax.dsa.xmpp;

import de.pax.dsa.connection.IIcarusSession;
import de.pax.dsa.model.PositionUpdate;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jxmpp.stringprep.XmppStringprepException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by swinter on 22.04.2017.
 */
public class XmppIcarusSession implements IIcarusSession {
    private Logger logger = LoggerFactory.getLogger(XmppIcarusSession.class);

    private XmppManager xmppManager;

    @Override
    public void connect(String user, String password) {
        try {
            xmppManager = new XmppManager("jabber.de" , user, password);
        } catch (XMPPException | IOException | InterruptedException | SmackException e) {
            logger.error("Unable to connect", e);
        }
    }

    @Override
    public void onPositionUpdate(Consumer<PositionUpdate> positionUpdateConsumer) {

    }

    @Override
    public void sendPositionUpdate(PositionUpdate positionUpdate) {
    	try {
			xmppManager.sendMessage(positionUpdate.toString(), "pax1@jabber.de");
		} catch (XmppStringprepException | NotConnectedException | XMPPException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
