package messages;

import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.SearchAirportRouteMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestSearchRoute {

	@Test
	public void constructBody1() {
		Airport from = new Airport("1", "ARN", "Arlanda");
		Airport to = new Airport("2", "FOO", "FooBar");

		String[] body = SearchAirportRouteMsg.constructBody(from, to);

		String[] expected = new String[] {
			"1", "ARN", "Arlanda",
			"2", "FOO", "FooBar"
		};

		assertArrayEquals(expected, body);
	}

	@Test
	public void parse1() throws UnsupportedEncodingException, MalformedMessageException {
		String data = "12&123&1&ARN&Arlanda&2&FOO&FooBar&";

		SearchAirportRouteMsg msg = (SearchAirportRouteMsg) Message.parseMessage(data);

		Airport from = new Airport("1", "ARN", "Arlanda");
		Airport to = new Airport("2", "FOO", "FooBar");

		assertEquals(from.getAirportID(), msg.getFrom().getAirportID());
		assertEquals(from.getIATA(), msg.getFrom().getIATA());
		assertEquals(from.getName(), msg.getFrom().getName());

		assertEquals(to.getAirportID(), msg.getTo().getAirportID());
		assertEquals(to.getIATA(), msg.getTo().getIATA());
		assertEquals(to.getName(), msg.getTo().getName());
	}

	@Test
	public void create1() throws UnsupportedEncodingException {
		Airport from = new Airport("1", "ARN", "Arlanda");
		Airport to = new Airport("2", "FOO", "FooBar");

		SearchAirportRouteMsg msg = new SearchAirportRouteMsg(1337L, SearchAirportRouteMsg.constructBody(from, to));

		assertEquals("12&1337&1&ARN&Arlanda&2&FOO&FooBar&", msg.createMessage());
	}
}
