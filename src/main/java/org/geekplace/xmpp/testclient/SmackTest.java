package org.geekplace.xmpp.testclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

public abstract class SmackTest<C extends XMPPConnection> {
	public static String SERV;
	public static String USER;
	public static String PASS;
	public static String BOSH_SERV;


	protected C connection;

	protected final void runTest() throws Exception {
		loadProperties();
		System.out.println("Using Smack Version: " + SmackConfiguration.getVersion());
		runTestSubclass();
		System.out.println("Test run without Exception");
	}

	protected abstract void runTestSubclass() throws Exception;

	static void loadProperties() throws IOException {
		Properties properties = new Properties();
		FileInputStream in = new FileInputStream("properties");
		try {
			properties.load(in);
		} finally {
			in.close();
		}
		SERV = properties.getProperty("serv");
		USER = properties.getProperty("user");
		PASS = properties.getProperty("pass");
		BOSH_SERV = properties.getProperty("boshserv");
	}

	protected void send(String message) throws NotConnectedException, InterruptedException {
		Message m = new Message("flo@freakempire.de");
		m.setType(Type.chat);
		m.setBody(message);
		connection.sendPacket(m);
		Thread.sleep(2000);
	}
}
