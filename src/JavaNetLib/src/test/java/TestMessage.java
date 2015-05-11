import org.junit.Test;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeMsg;
import ospp.bookinggui.networking.messages.HandshakeRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

public class TestMessage {

	@Test
	public void deconHandshake1() throws UnsupportedEncodingException {
		String username = "Tjenare";
		String password = "foobar";

		byte[] usr = username.getBytes(Message.ENCODING);
		byte[] pas = password.getBytes(Message.ENCODING);

		byte[] message = new byte[4 + usr.length + pas.length];

		int index = 0;

		message[index++] = (byte) ((usr.length & 0xFF00) >> 8);
		message[index++] = (byte) (usr.length & 0x00FF);
		System.arraycopy(usr, 0, message, index, usr.length);

		index += usr.length;

		message[index++] = (byte) ((pas.length & 0xFF00) >> 8);
		message[index++] = (byte) (pas.length & 0x00FF);
		System.arraycopy(pas, 0, message, index, pas.length);

		Message msg = Message.parseMessage((short) 1, Utils.convertByteArrayToInt(message));

		assertTrue(msg instanceof HandshakeMsg);

		HandshakeMsg handshake = (HandshakeMsg) msg;

		assertTrue(handshake.getPassword().equals("foobar"));
		assertTrue(handshake.getUsername().equals("Tjenare"));
	}

	@Test
	public void deconHandshake2() throws UnsupportedEncodingException {
		HandshakeMsg hand_msg = new HandshakeMsg("Peter", "Salming");

		byte[] hand_msg_ar = hand_msg.constructBody();

		Message decon = Message.parseMessage((short) 1, Utils.convertByteArrayToInt(hand_msg_ar));

		assertTrue(decon instanceof HandshakeMsg);

		HandshakeMsg decon_msg = (HandshakeMsg) decon;

		assertTrue(decon_msg.getUsername().equals(hand_msg.getUsername()));
		assertTrue(decon_msg.getPassword().equals(hand_msg.getPassword()));
	}

	@Test
	public void deconHandshakeResp() throws UnsupportedEncodingException {

		int[] hand_resp_success = new int[] {
			0xFF
		};

		int[] hand_resp_failure = new int[] {
			0x00
		};

		Message hand_resp_success_msg = Message.parseMessage((short) 2, hand_resp_success);
		Message hand_resp_failure_msg = Message.parseMessage((short) 2, hand_resp_failure);

		assertTrue(hand_resp_failure_msg instanceof HandshakeRespMsg);
		assertTrue(hand_resp_success_msg instanceof HandshakeRespMsg);

		assertTrue(!((HandshakeRespMsg) hand_resp_failure_msg).isSuccessful());
		assertTrue(((HandshakeRespMsg) hand_resp_success_msg).isSuccessful());
	}
}
