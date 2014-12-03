package eu.geekplace.xmpp.testclient;

import java.io.IOException;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.bosh.BOSHConfiguration;
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
		BOSHConfiguration conf = BOSHConfiguration.builder().setUsernameAndPassword(USER, PASS)
				.setFile("/http-bind/").setHost(BOSH_SERV).setPort(7070).setServiceName(SERV)
				.build();
		connection = new XMPPBOSHConnection(conf);

		connection.connect();

		connection.login();

		send("Hi, what's up?");

		connection.disconnect();
	}

}
