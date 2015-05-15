package messages;

import org.junit.Test;
import ospp.bookinggui.Passenger;
import ospp.bookinggui.networking.messages.GetPassengerInfoMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertArrayEquals;

public class TestGetPassengerInfo {

	@Test
	public void parse() {

	}

	@Test
	public void constructBody() throws UnsupportedEncodingException {
		Passenger p = new Passenger(0x0102030405060708L, "Tjenare", null, null, null, null, 0);

		GetPassengerInfoMsg msg = new GetPassengerInfoMsg(p);

		byte[] body = msg.constructBody();

		byte[] expected = new byte[] {
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
		};

		assertArrayEquals(expected, body);
	}
}
