import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Date;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestFlight {

	@Test
	public void createBody1() {

		Airport airport = new Airport("12", "ARN", "Arlanda");

		Date date = new Date("1992", "03", "31", "12", "00", "00");

		Flight f = new Flight("TJOFRÄS", airport, date, "2");

		String[] expected_body = new String[] {
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"1992", "03", "31", "12", "00", "00",
			"2"
		};

		assertArrayEquals(expected_body, f.createBody());
	}

	@Test
	public void parse1() {
		String[] data = new String[] {
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"1992", "03", "31", "12", "00", "00",
			"2"
		};

		Flight f = Flight.parseBody(data, 0);

		assertEquals("TJOFRÄS", f.getFlightID());

		Airport a = f.getAirport();
		Date d = f.getDate();

		assertEquals("12", a.getAirportID());
		assertEquals("ARN", a.getIATA());
		assertEquals("Arlanda", a.getName());

		assertEquals("1992", d.getYear());
		assertEquals("03", d.getMonth());
		assertEquals("31", d.getDay());
		assertEquals("12", d.getHour());
		assertEquals("00", d.getMinute());
		assertEquals("00", d.getSecond());
	}
}
