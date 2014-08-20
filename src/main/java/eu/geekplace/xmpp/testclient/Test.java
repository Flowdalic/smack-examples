package eu.geekplace.xmpp.testclient;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;

public class Test {

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new TCPXEP198Test();
		test.runTest();
	}

}
