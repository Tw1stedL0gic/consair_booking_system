import org.junit.Test;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeResponse;

import static org.junit.Assert.assertTrue;

public class TestMessage {

	@Test
	public void deconHandshake() {
		String username = "Tjenare";
		String password = "foobar";

		byte[] usr = username.getBytes();
		byte[] pas = password.getBytes();

		byte[] message = new byte[4 + usr.length + pas.length];

		
	}

	@Test
	public void deconHandshakeResp() {
		byte[] hand_resp_success = new byte[] {
			(byte) 0xFF
		};

		byte[] hand_resp_failure = new byte[] {
			0x00
		};

		Message hand_resp_success_msg = Message.deconstructMessage((short) 2, hand_resp_success);
		Message hand_resp_failure_msg = Message.deconstructMessage((short) 2, hand_resp_failure);

		assertTrue(hand_resp_failure_msg instanceof HandshakeResponse);
		assertTrue(hand_resp_success_msg instanceof HandshakeResponse);

		assertTrue(((HandshakeResponse) hand_resp_failure_msg).isSuccessfull() == false);
		assertTrue(((HandshakeResponse) hand_resp_success_msg).isSuccessfull() == true);
	}
}
