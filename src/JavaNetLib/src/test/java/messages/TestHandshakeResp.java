package messages;

import org.junit.Test;
import ospp.bookinggui.Utils;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeRespMsg;

import java.io.UnsupportedEncodingException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

public class TestHandshakeResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		HandshakeRespMsg handresp = new HandshakeRespMsg(true);
		byte[] message = handresp.constructBody();

		int[] con = Utils.convertByteArrayToInt(message);

		Message msg = Message.parseMessage((short) 2, con);

		assertTrue(msg instanceof HandshakeRespMsg);

		HandshakeRespMsg retrieved = (HandshakeRespMsg) msg;

		assertTrue(retrieved.isSuccessful());
	}

	@Test
	public void parse2() throws UnsupportedEncodingException {
		int[] msg_array = new int[0];

		try {
			Message msg = Message.parseMessage((short) 2, msg_array);
			fail("Message.parseMessage() did not throw MalformedMessageException!");
		}
		catch(MalformedMessageException e) {
		}
	}
}
