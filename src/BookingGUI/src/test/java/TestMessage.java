import org.junit.Test;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeMSG;
import ospp.bookinggui.networking.messages.HandshakeResponseMSG;

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

		Message msg = Message.parseMessage((short) 1, Utils.convertByteArrayToInt(message), Message.ENCODING);

		assertTrue(msg instanceof HandshakeMSG);

		HandshakeMSG handshake = (HandshakeMSG) msg;

		assertTrue(handshake.getPassword().equals("foobar"));
		assertTrue(handshake.getUsername().equals("Tjenare"));
	}

	@Test
	public void deconHandshake2() throws UnsupportedEncodingException {
		HandshakeMSG hand_msg = new HandshakeMSG("Peter", "Salming");

		byte[] hand_msg_ar = hand_msg.constructBody();

		Message decon = Message.parseMessage((short) 1, Utils.convertByteArrayToInt(hand_msg_ar), Message.ENCODING);

		assertTrue(decon instanceof HandshakeMSG);

		HandshakeMSG decon_msg = (HandshakeMSG) decon;

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

		Message hand_resp_success_msg = Message.parseMessage((short) 2, hand_resp_success, Message.ENCODING);
		Message hand_resp_failure_msg = Message.parseMessage((short) 2, hand_resp_failure, Message.ENCODING);

		assertTrue(hand_resp_failure_msg instanceof HandshakeResponseMSG);
		assertTrue(hand_resp_success_msg instanceof HandshakeResponseMSG);

		assertTrue(!((HandshakeResponseMSG) hand_resp_failure_msg).isSuccessful());
		assertTrue(((HandshakeResponseMSG) hand_resp_success_msg).isSuccessful());
	}
}
