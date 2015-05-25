package messages;

import org.junit.Test;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.RequestFlightDetailsRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class TestRequestFlightDetailsResp {

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "15&1337&" +
			"1&" +
			"1&ARN&Arlanda&" +
			"2&FOO&FooBar&" +
			"1992&03&31&12&00&00&" +
			"2015&05&25&12&00&00&" +
			"3&" +
			"1&3&FIRST&true&12&12&false&" +
			"2&3&ECO&true&12&13&false&";

		RequestFlightDetailsRespMsg msg = (RequestFlightDetailsRespMsg) Message.parseMessage(data);

		Flight flight = msg.getFlight();

		//TODO test flight as well!

		Seat[] seats = msg.getSeatList();

		assertEquals(2, seats.length);

		Seat s1 = seats[0];
		Seat s2 = seats[1];

		assertEquals("1", s1.getSeatID());
		assertEquals("3", s1.getFlightID());
		assertEquals("FIRST", s1.getKlass());
		assertEquals(true, s1.getWindow());
		assertEquals(12, s1.getRow());
		assertEquals(12, s1.getCol());
		assertEquals(false, s1.isLocked());

		assertEquals("2", s2.getSeatID());
		assertEquals("3", s2.getFlightID());
		assertEquals("ECO", s2.getKlass());
		assertEquals(true, s2.getWindow());
		assertEquals(12, s2.getRow());
		assertEquals(13, s2.getCol());
		assertEquals(false, s2.isLocked());
	}

	//TODO ADD CRASH TESTS!
}
