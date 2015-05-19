import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.LoginMsg;

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

	@Test
	public void constructMessage1() {

		long timestamp = 1337L;

		LoginMsg loginMsg = new LoginMsg(timestamp, "göran", "persson");

		String data = loginMsg.createMessage();

		assertEquals("1&1337&göran&persson&", data);
	}

	@Test
	public void constructMessage2() {
		long timestamp = 1338L;

		Message msg = new Message(MessageType.LOGIN, timestamp, "arg1", "arg2", "arg3");

		String data = msg.createMessage();

		assertEquals("1&1338&arg1&arg2&arg3&", data);
	}

	@Test
	public void parseIncorrectData() {
		String data = "asddisfuhs839i2no34";

		try {
			Message.parseMessage(data);
			fail("Parsing with incorrect data did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("Could not parse message! The message is too small!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectID() {
		String data = "sdflk&lkaksjd&";

		try {
			Message.parseMessage(data);
			fail("Parsing with incorrect id did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("The message ID is not an integer!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectID2() {
		String data = "1231241422&1231243245&";

		try {
			Message.parseMessage(data);
			fail("Parsing with unsupported id did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("The supplied message ID is not supported!", e.getMessage());
		}
	}

	@Test
	public void parseIncorrectTimestamp() {
		String data = "1&234234jkj24&";

		try {
			Message.parseMessage(data);
			fail("Parsing with incorrect timestamp did not throw an exception!");
		}
		catch(MalformedMessageException e) {
			assertEquals("The timestamp is not a valid long!", e.getMessage());
		}
	}
}