package eu.geekplace.xmpp.testclient;

import java.util.List;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

public class MUCTest extends SmackTest<XMPPTCPConnection> {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new MUCTest();
		test.runTest();
	}

	@Override
	protected void runTestSubclass() throws Exception {
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
		
		MultiUserChatManager mucm = MultiUserChatManager.getInstanceFor(connection);
		List<String> services = mucm.getServiceNames();

		if (services.isEmpty()) {
			throw new Exception("No MUC services found");
		}
		String service = services.get(0);
		MultiUserChat muc = mucm.getMultiUserChat("smacktestfoobar@" + service);

		muc.join("smacktest");
		muc.sendMessage("Hello, how are you?");

		Thread.sleep(1000);

		muc.leave();

		connection.disconnect();
		System.out.println("Disconnected without Exception");
	}
}
