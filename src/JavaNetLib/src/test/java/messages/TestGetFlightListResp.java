package messages;

import org.junit.Test;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.GetFlightListRespMsg;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetFlightListResp {

	@Test
	public void parse1() throws UnsupportedEncodingException {
		byte[] arg1 = "Test1".getBytes(Message.ENCODING);

		byte[] body1 = new byte[Message.AL_SIZE + arg1.length];

		Message.setALValue(body1, arg1.length, 0);
		Message.setArgument(body1, arg1, Message.AL_SIZE);

		GetFlightListRespMsg msg1 = GetFlightListRespMsg.parse(Utils.convertByteArrayToInt(body1));

		ArrayList<Flight> flights1 = msg1.getFlightList();

		assertTrue(flights1.size() == 1);

		assertEquals("Test1", flights1.get(0).getFlightNumber());
	}

	@Test
	public void parse2() throws UnsupportedEncodingException {
		byte[] arg1 = "Test1".getBytes(Message.ENCODING);
		byte[] arg2 = "Test2".getBytes(Message.ENCODING);

		byte[] body = new byte[Message.AL_SIZE * 2 + arg1.length + arg2.length];

		Message.setALValue(body, arg1.length, 0);
		Message.setArgument(body, arg1, Message.AL_SIZE);

		Message.setALValue(body, arg2.length, Message.AL_SIZE + arg1.length);
		Message.setArgument(body, arg2, Message.AL_SIZE * 2 + arg1.length);

		GetFlightListRespMsg msg = GetFlightListRespMsg.parse(Utils.convertByteArrayToInt(body));

		ArrayList<Flight> flights = msg.getFlightList();

		assertTrue(flights.size() == 2);

		Flight f1 = flights.get(0);
		Flight f2 = flights.get(1);

		assertEquals("Test1", f1.getFlightNumber());
		assertEquals("Test2", f2.getFlightNumber());
	}

	@Test
	public void constructBody() {

	}
}
