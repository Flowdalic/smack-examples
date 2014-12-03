package eu.geekplace.xmpp.testclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration.XMPPTCPConnectionConfigurationBuilder;
import org.jivesoftware.smack.util.TLSUtils;

public class TCPXEP198Test extends SmackTest<XMPPTCPConnection> {
	
	static {
		SmackConfiguration.setDefaultPacketReplyTimeout(1000*60*5);
		SmackConfiguration.DEBUG_ENABLED = true;
	}

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new TCPXEP198Test();
		test.runTest();
	}

	@Override
	protected void runTestSubclass() throws KeyManagementException, NoSuchAlgorithmException, SmackException, IOException, XMPPException, InterruptedException {
		XMPPTCPConnectionConfigurationBuilder conf = XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(SERV);
		conf.setUsernameAndPassword(USER, PASS);
		conf.setSecurityMode(SecurityMode.disabled);
//		conf.setLegacySessionDisabled(true);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);
		connection = new XMPPTCPConnection(conf.build());
		connection.setUseStreamManagement(true);

		connection.connect();

		connection.login();

		send("Hi, what's up?");

		connection.instantShutdown();

		send("Hi, what's up? I've been just instantly shutdown");

		// Reconnect with xep198
		connection.connect();

		send("Hi, what's up? I've been just resumed");

		connection.disconnect();
	}
}
