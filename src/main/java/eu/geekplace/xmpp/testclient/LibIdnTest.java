package eu.geekplace.xmpp.testclient;

import gnu.inet.encoding.Stringprep;

public class LibIdnTest {

	public static void main(String args[]) throws Exception {
		String idnDomain = "éxample.net";
		String nameprepedidnDomain = Stringprep.nameprep(idnDomain);
		System.out.println("IDN Domain: " + idnDomain);
		System.out.println("Namepreped IDN Domain: " + nameprepedidnDomain);
	}

}
