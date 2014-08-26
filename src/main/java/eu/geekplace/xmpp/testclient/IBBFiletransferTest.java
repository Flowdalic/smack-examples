package eu.geekplace.xmpp.testclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jxmpp.util.XmppStringUtils;

public class IBBFiletransferTest extends SmackTest<XMPPTCPConnection> {
	
	static {
		SmackConfiguration.setDefaultPacketReplyTimeout(1000*60*5);
		SmackConfiguration.DEBUG_ENABLED = true;
		FileTransferNegotiator.IBB_ONLY = true;
	}

	public static void main(String args[]) throws Exception {
		SmackTest<XMPPTCPConnection> test = new IBBFiletransferTest();
		test.runTest();
	}

	private static final byte[] dataToSend = StringUtils.randomString(1024 * 4 * 3).getBytes();
	private static byte[] dataReceived;

	@Override
	protected void runTestSubclass() throws SmackException, IOException,
			XMPPException, InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		System.out.println("Using Smack Version: " + SmackConfiguration.getVersion());
		ConnectionConfiguration conf = new ConnectionConfiguration(SERV);
		conf.setSecurityMode(SecurityMode.disabled);
		conf.setLegacySessionDisabled(true);
		conf.setCompressionEnabled(true);
		TLSUtils.acceptAllCertificates(conf);

		connection = new XMPPTCPConnection(conf);
		connection.connect();
		connection.login(USER, PASS, "sender");
		
		XMPPTCPConnection connection2 = new XMPPTCPConnection(conf);
		connection2.connect();
		connection2.login(USER, PASS, "receiver");
		
		FileTransferManager ftm1 = FileTransferManager.getInstanceFor(connection);
		FileTransferManager ftm2 = FileTransferManager.getInstanceFor(connection2);
		
		ftm2.addFileTransferListener(new FileTransferListener() {
			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				IncomingFileTransfer ift = request.accept();
				try {
					InputStream is = ift.recieveFile();
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					int nRead;
					byte[] buf = new byte[1024];
					while ((nRead = is.read(buf,  0, buf.length)) != -1) {
						os.write(buf, 0, nRead);
					}
					os.flush();
					dataReceived = os.toByteArray();
				} catch (SmackException | IOException | XMPPErrorException e) {
					e.printStackTrace();
				}
				if (Arrays.equals(dataToSend, dataReceived)) {
					System.out.println("Received data matches send data. \\o/");
				} else {
					System.err.println("Recieved data DOES NOT match send data. :(");
				}
			}
		});

		OutgoingFileTransfer oft = ftm1.createOutgoingFileTransfer(XmppStringUtils.completeJidFrom(USER, SERV, "receiver"));
		oft.sendStream(new ByteArrayInputStream(dataToSend), "hello.txt", dataToSend.length, "A greeting");
		outerloop: while (!oft.isDone()) {
			switch (oft.getStatus()) {
			case error:
				System.out.println("Filetransfer error: " + oft.getError());
				break outerloop;
			default:
				System.out.println("Filetransfer status: " + oft.getStatus() + ". Progress: " + oft.getProgress());
				break;
			}
			Thread.sleep(1000);
		}
		
		connection.disconnect();
		connection2.disconnect();
		Thread.sleep(1000);
	}
}
