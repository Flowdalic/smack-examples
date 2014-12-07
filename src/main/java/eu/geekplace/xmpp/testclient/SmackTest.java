package eu.geekplace.xmpp.testclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
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
	public static String OTHER_ENTITY;
	public static boolean DEBUG;

	protected C connection;

	static {
		// JUL Debugger will not print any information until configured to print log messages of level FINE
		SmackConfiguration.addDisabledSmackClass("org.jivesoftware.smack.debugger.JulDebugger");
	}

	protected final void runTest() throws Exception {
		loadProperties();
		System.out.println("Using Smack Version: " + SmackConfiguration.getVersion());
		SmackConfiguration.DEBUG_ENABLED = DEBUG;
		runTestSubclass();
		System.out.println("Test run without Exception");
	}

	protected abstract void runTestSubclass() throws Exception;

	static void loadProperties() throws IOException {
		File propertiesFile = findPropertiesFile();
		Properties properties = new Properties();
		try (FileInputStream in = new FileInputStream(propertiesFile)) {
			properties.load(in);
		}
		SERV = properties.getProperty("serv");
		USER = properties.getProperty("user");
		PASS = properties.getProperty("pass");
		BOSH_SERV = properties.getProperty("boshserv");
		OTHER_ENTITY = properties.getProperty("otherentity");
		String debug = properties.getProperty("debug");
		if (debug != null) {
			DEBUG = Boolean.valueOf(debug);
		}
	}

	protected void send(String message) throws NotConnectedException, InterruptedException {
		Message m = new Message(OTHER_ENTITY);
		m.setType(Type.chat);
		m.setBody(message);
		connection.sendPacket(m);
		Thread.sleep(2000);
	}

	private static File findPropertiesFile() throws IOException {
		List<String> possibleLocations = new LinkedList<String>();
		possibleLocations.add("properties");
		String userHome = System.getProperty("user.home");
		if (userHome != null) {
			possibleLocations.add(userHome + "/.config/smack-examples/properties");
		}
		for (String possibleLocation : possibleLocations) {
			File res = new File(possibleLocation);
			if (res.isFile())
				return res;
		}
		throw new IOException("Could not find properties file: Searched locations");
	}
}
