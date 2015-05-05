package messages;

import org.junit.Test;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeResponseMSG;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

public class TestHandshakeResp {

	@Test
	public void test1() throws UnsupportedEncodingException {
		HandshakeResponseMSG handresp = new HandshakeResponseMSG(true);
		byte[] message = handresp.constructBody();

		int[] con = Utils.convertByteArrayToInt(message);

		Message msg = Message.parseMessage((short) 2, con, Message.ENCODING);

		assertTrue(msg instanceof HandshakeResponseMSG);

		HandshakeResponseMSG retrieved = (HandshakeResponseMSG) msg;

		assertTrue(retrieved.isSuccessful());
	}
}
