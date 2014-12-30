package eu.geekplace.xmpp.testclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;

public class TCPSASLProvidedTest extends SmackTest<XMPPTCPConnection> {
	
	static {
		// Disable JavaX SASL mechs, so that the smack provided ones are used
		SASLAuthentication.unregisterSASLMechanism("org.jivesoftware.smack.sasl.javax.SASLDigestMD5Mechanism");
	}

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new TCPSASLProvidedTest();
		test.runTest();
	}

	@Override
	protected void runTestSubclass()throws SmackException, IOException,
			XMPPException, InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		System.out.println("Using Smack Version: " + SmackConfiguration.getVersion());
		XMPPTCPConnectionConfiguration.Builder conf = XMPPTCPConnectionConfiguration.builder();
		conf.setServiceName(SERV);
		conf.setUsernameAndPassword(USER, PASS);
		conf.setSecurityMode(SecurityMode.disabled);
//		conf.setLegacySessionDisabled(true);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);
		connection = new XMPPTCPConnection(conf.build());

		connection.connect();

		connection.login();

		send("Hi, what's up?");

		connection.disconnect();
		System.out.println("Disconnected without Exception");
	}
}
