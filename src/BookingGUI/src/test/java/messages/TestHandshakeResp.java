package messages;

import org.junit.Test;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeResponse;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

public class TestHandshakeResp {

	@Test
	public void test1() throws UnsupportedEncodingException {
		HandshakeResponse handresp = new HandshakeResponse(true);
		byte[] message = handresp.constructBody();

		int[] con = Utils.convertByteArrayToInt(message);

		Message msg = Message.parseMessage((short) 2, con, Message.ENCODING);

		assertTrue(msg instanceof HandshakeResponse);

		HandshakeResponse retrieved = (HandshakeResponse) msg;

		assertTrue(retrieved.isSuccessful());
	}
}
