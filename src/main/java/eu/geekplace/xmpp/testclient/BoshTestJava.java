package eu.geekplace.xmpp.testclient;

import java.io.IOException;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.bosh.XMPPBOSHConnection;

public class BoshTestJava extends SmackTest<XMPPBOSHConnection> {

	static {
		SmackConfiguration.DEBUG_ENABLED = true;
		SmackConfiguration.setDefaultPacketReplyTimeout(1000*60*5);
	}

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPBOSHConnection> test = new BoshTestJava();
		test.runTest();
	}

	@Override
	protected void runTestSubclass()  throws SmackException, IOException,
			XMPPException, InterruptedException {
		connection = new XMPPBOSHConnection(false, SmackTest.BOSH_SERV,
				7070, "/http-bind/", SmackTest.SERV);

		connection.connect();

		connection.login(SmackTest.USER, SmackTest.PASS);

		send("Hi, what's up?");

		connection.disconnect();
	}

}
