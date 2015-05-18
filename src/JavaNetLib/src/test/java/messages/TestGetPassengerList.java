package messages;

import org.junit.Test;
import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.GetPassengerListMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetPassengerList {

	@Test
	public void parse() throws UnsupportedEncodingException {
		byte[] flight = "test123".getBytes(Message.ENCODING);

		byte[] m1 = new byte[Message.AL_SIZE + flight.length];

		Message.setALValue(m1, flight.length, 0);
		Message.setArgument(m1, flight, Message.AL_SIZE);

		GetPassengerListMsg msg = GetPassengerListMsg.parse(m1);

		assertTrue(msg != null);

		Flight f1 = msg.getFlight();

		assertEquals("test123", f1.getFlightNumber());
	}

	@Test
	public void constructBody() throws UnsupportedEncodingException {
		Flight f = new Flight("Tjosan123");

		GetPassengerListMsg msg1 = new GetPassengerListMsg(f);

		byte[] m1 = msg1.constructBody();

		assertTrue(m1.length != 0);

		int al1 = Message.getALValue(m1, 0);
		byte[] a1 = Message.getArgument(m1, al1, Message.AL_SIZE);

		assertTrue(al1 != 0);
		assertTrue(al1 == a1.length);

		String s1 = new String(a1, Message.ENCODING);

		assertEquals("Tjosan123", s1);
	}
}
