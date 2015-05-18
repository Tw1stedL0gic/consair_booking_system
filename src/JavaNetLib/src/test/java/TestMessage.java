import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeMsg;
import ospp.bookinggui.networking.messages.HandshakeRespMsg;
import ospp.bookinggui.networking.messages.HeartbeatMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class TestMessage {

	@Test
	public void setALValue() {
		int al = 12;
		byte[] message = new byte[Message.AL_SIZE];

		Message.setALValue(message, al, 0);

		byte[] expected = new byte[Message.AL_SIZE];
		expected[Message.AL_SIZE - 1] = 12;

		assertArrayEquals(expected, message);
	}

	@Test
	public void getALValue() {
		byte[] m = new byte[]{
			0x12, 0x12
		};

		int value = Message.getALValue(m, 0);

		assertEquals(0x1212, value);

		byte[] m2 = new byte[]{
			0x01, 0x02, 0x03, 0x04, 0x05
		};

		int value21 = Message.getALValue(m2, 1);

		assertEquals(0x0203, value21);

		int value22 = Message.getALValue(m2, 3);

		assertEquals(0x0405, value22);
	}

	@Test
	public void setArgument() {
		byte[] m = new byte[]{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		};

		Message.setArgument(m, new byte[]{0x12, 0x13}, 0);

		assertArrayEquals(new byte[]{0x12, 0x13, 0, 0, 0, 0, 0, 0, 0, 0}, m);

		byte[] m2 = new byte[]{
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0
		};

		Message.setArgument(m2, new byte[]{0x12, 0x13}, 3);

		assertArrayEquals(new byte[]{0, 0, 0, 0x12, 0x13, 0, 0, 0, 0, 0}, m2);
	}

	@Test
	public void constructHeader() {

		Message foo = new HeartbeatMsg();

		// Test with a zero body size
		byte[] header = foo.constructHeader(0);

		byte[] expected = new byte[]{
			0, 0, 0, 1, Message.Type.HEARTBEAT.ID
		};

		assertArrayEquals(expected, header);


		// Test with a >zero body size
		byte[] header2 = foo.constructHeader(0x0f1232fa);

		byte[] expected2 = new byte[]{
			0x0f, 0x12, 0x32, (byte) 0xfa + 1, Message.Type.HEARTBEAT.ID
		};

		assertArrayEquals(expected2, header2);
	}

	@Test
	public void deconHandshake1() throws UnsupportedEncodingException, MalformedMessageException {
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

		Message msg = Message.parseMessage((short) 1, message);

		assertTrue(msg instanceof HandshakeMsg);

		HandshakeMsg handshake = (HandshakeMsg) msg;

		assertTrue(handshake.getPassword().equals("foobar"));
		assertTrue(handshake.getUsername().equals("Tjenare"));
	}

	@Test
	public void deconHandshake2() throws UnsupportedEncodingException, MalformedMessageException {
		HandshakeMsg hand_msg = new HandshakeMsg("Peter", "Salming");

		byte[] hand_msg_ar = hand_msg.constructBody();

		Message decon = Message.parseMessage((short) 1, hand_msg_ar);

		assertTrue(decon instanceof HandshakeMsg);

		HandshakeMsg decon_msg = (HandshakeMsg) decon;

		assertTrue(decon_msg.getUsername().equals(hand_msg.getUsername()));
		assertTrue(decon_msg.getPassword().equals(hand_msg.getPassword()));
	}

	@Test
	public void deconHandshakeResp() throws UnsupportedEncodingException, MalformedMessageException {

		byte[] hand_resp_success = new byte[]{
			(byte) 0xFF
		};

		byte[] hand_resp_failure = new byte[]{
			0x00
		};

		Message hand_resp_success_msg = Message.parseMessage((short) 2, hand_resp_success);
		Message hand_resp_failure_msg = Message.parseMessage((short) 2, hand_resp_failure);

		assertTrue(hand_resp_failure_msg instanceof HandshakeRespMsg);
		assertTrue(hand_resp_success_msg instanceof HandshakeRespMsg);

		assertTrue(!((HandshakeRespMsg) hand_resp_failure_msg).isSuccessful());
		assertTrue(((HandshakeRespMsg) hand_resp_success_msg).isSuccessful());
	}

	@Test
	public void createPaidBlock() {
		final long paid = 0x1112131415161718L;

		byte[] block = Message.createPaidBlock(paid);

		byte[] expected = new byte[]{
			0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
		};

		assertArrayEquals(expected, block);

		final long paid2 = 0xff00ff00ff00ff00L;

		byte[] block2 = Message.createPaidBlock(paid2);

		byte[] expected2 = new byte[]{
			(byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff, 0x00
		};

		assertArrayEquals(expected2, block2);

		final long paid3 = 0x00ff00ff00ff00ffL;

		byte[] block3 = Message.createPaidBlock(paid3);

		byte[] expected3 = new byte[]{
			0x00, (byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff, 0x00, (byte) 0xff,
		};

		assertArrayEquals(expected3, block3);
	}

	@Test
	public void testParseRandomData() {
		byte[] data = new byte[] {
			0x23, 0x21, 0x65, (byte) 0xf1, (byte) 0x88, 0x76
		};

		try {
			Message msg = Message.parseMessage((short) 0, data);
			fail("Message.parseMessage did not throw the correct exceptions with random data!");
		}
		catch(MalformedMessageException ignored) {
		}
		catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
