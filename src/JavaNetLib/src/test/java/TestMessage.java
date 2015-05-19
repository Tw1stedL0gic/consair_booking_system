import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

import static org.junit.Assert.*;

public class TestMessage {

	@Test
	public void parseRandomData1() {
		byte[] data = new byte[]{
			0x23, 0x21, 0x65, (byte) 0xf1, (byte) 0x88, 0x76
		};

		try {
			Message.parseMessage(new String(data));
			fail("Message.parseMessage did not throw the correct exceptions with random data!");
		}
		catch(MalformedMessageException ignored) {
		}
	}

	@Test
	public void testParseLogin() throws MalformedMessageException {
		String data = "1&100203002&tjenare&yolo&";

		Message msg = Message.parseMessage(data);

		assertEquals(MessageType.LOGIN, msg.getType());
		assertEquals(100203002L, msg.getTimestamp());

		String[] body = msg.getBody();

		assertArrayEquals(new String[]{"tjenare", "yolo"}, body);
	}
}