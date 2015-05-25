package messages;

import org.junit.Test;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.LoginRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class TestLoginResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "2&126&ADMIN&";

		LoginRespMsg msg = (LoginRespMsg) Message.parseMessage(data);

		assertEquals("ADMIN", msg.getPrivilegeLeve());
	}
}
