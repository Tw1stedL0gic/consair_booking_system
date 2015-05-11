package messages;

import org.junit.Test;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

public class TestHandshakeResp {

	@Test
	public void test1() throws UnsupportedEncodingException {
		HandshakeRespMsg handresp = new HandshakeRespMsg(true);
		byte[] message = handresp.constructBody();

		int[] con = Utils.convertByteArrayToInt(message);

		Message msg = Message.parseMessage((short) 2, con);

		assertTrue(msg instanceof HandshakeRespMsg);

		HandshakeRespMsg retrieved = (HandshakeRespMsg) msg;

		assertTrue(retrieved.isSuccessful());
	}
}
