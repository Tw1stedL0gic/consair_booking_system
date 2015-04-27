import org.junit.Test;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.Handshake;
import ospp.bookinggui.networking.messages.HandshakeResponse;

import static org.junit.Assert.assertTrue;

public class TestMessage {

	@Test
	public void deconHandshake() {
		String username = "Tjenare";
		String password = "foobar";

		byte[] usr = username.getBytes();
		byte[] pas = password.getBytes();

		short[] message = new short[4 + usr.length + pas.length];

		int index = 0;

		message[index++] = (short) ((usr.length & 0xFF00) >> 8);
		message[index++] = (short) (usr.length & 0x00FF);
		System.arraycopy(usr, 0, message, index, usr.length);

		index += usr.length;

		message[index++] = (short) ((pas.length & 0xFF00) >> 8);
		message[index++] = (short) (pas.length & 0x00FF);
		System.arraycopy(pas, 0, message, index, pas.length);

		Message msg = Message.deconstructMessage((short) 1, message);

		assertTrue(msg instanceof Handshake);

		Handshake handshake = (Handshake) msg;

		assertTrue(handshake.getPassword().equals("foobar"));
		assertTrue(handshake.getUsername().equals("Tjenare"));
	}

	@Test
	public void deconHandshakeResp() {

		short[] hand_resp_success = new short[] {
			0xFF
		};

		short[] hand_resp_failure = new short[] {
			0x00
		};

		Message hand_resp_success_msg = Message.deconstructMessage((short) 2, hand_resp_success);
		Message hand_resp_failure_msg = Message.deconstructMessage((short) 2, hand_resp_failure);

		assertTrue(hand_resp_failure_msg instanceof HandshakeResponse);
		assertTrue(hand_resp_success_msg instanceof HandshakeResponse);

		assertTrue(!((HandshakeResponse) hand_resp_failure_msg).isSuccessfull());
		assertTrue(((HandshakeResponse) hand_resp_success_msg).isSuccessfull());
	}
}
