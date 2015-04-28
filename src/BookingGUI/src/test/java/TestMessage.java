import org.junit.Test;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.Handshake;
import ospp.bookinggui.networking.messages.HandshakeResponse;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertTrue;

@SuppressWarnings("WrongPackageStatement")
public class TestMessage {

	@Test
	public void deconHandshake1() throws UnsupportedEncodingException {
		String username = "Tjenare";
		String password = "foobar";

		byte[] usr = username.getBytes("UTF8");
		byte[] pas = password.getBytes("UTF8");

		byte[] message = new byte[4 + usr.length + pas.length];

		int index = 0;

		message[index++] = (byte) ((usr.length & 0xFF00) >> 8);
		message[index++] = (byte) (usr.length & 0x00FF);
		System.arraycopy(usr, 0, message, index, usr.length);

		index += usr.length;

		message[index++] = (byte) ((pas.length & 0xFF00) >> 8);
		message[index++] = (byte) (pas.length & 0x00FF);
		System.arraycopy(pas, 0, message, index, pas.length);

		Message msg = Message.deconstructMessage((short) 1, message, "UTF8");

		assertTrue(msg instanceof Handshake);

		Handshake handshake = (Handshake) msg;

		assertTrue(handshake.getPassword().equals("foobar"));
		assertTrue(handshake.getUsername().equals("Tjenare"));
	}

	@Test
	public void deconHandshake2() throws UnsupportedEncodingException {
		Handshake hand_msg = new Handshake("Peter", "Salming");

		byte[] hand_msg_ar = hand_msg.constructMessage();

		Message decon = Message.deconstructMessage((short) 1, hand_msg_ar, "UTF8");

		assertTrue(decon instanceof Handshake);

		Handshake decon_msg = (Handshake) decon;

		assertTrue(decon_msg.getUsername().equals(hand_msg.getUsername()));
		assertTrue(decon_msg.getPassword().equals(hand_msg.getPassword()));
	}

	@Test
	public void deconHandshakeResp() throws UnsupportedEncodingException {

		byte[] hand_resp_success = new byte[] {
			(byte) 0xFF
		};

		byte[] hand_resp_failure = new byte[] {
			0x00
		};

		Message hand_resp_success_msg = Message.deconstructMessage((short) 2, hand_resp_success, "UTF8");
		Message hand_resp_failure_msg = Message.deconstructMessage((short) 2, hand_resp_failure, "UTF8");

		assertTrue(hand_resp_failure_msg instanceof HandshakeResponse);
		assertTrue(hand_resp_success_msg instanceof HandshakeResponse);

		assertTrue(!((HandshakeResponse) hand_resp_failure_msg).isSuccessfull());
		assertTrue(((HandshakeResponse) hand_resp_success_msg).isSuccessfull());
	}
}
