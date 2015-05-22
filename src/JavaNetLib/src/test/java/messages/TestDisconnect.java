package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.messages.DisconnectMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class TestDisconnect {

	@Test
	public void parseCorrectData() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "3&1337247&greger&";

		Message msg = Message.parseMessage(data);

		assertTrue(msg instanceof DisconnectMsg);
		assertEquals(MessageType.DISCONNECT, msg.getType());
		assertEquals(1337247L, msg.getTimestamp());
	}
}
