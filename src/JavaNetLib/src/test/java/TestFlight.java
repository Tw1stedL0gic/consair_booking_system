import org.junit.Test;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Date;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;

import static org.junit.Assert.assertArrayEquals;

public class TestFlight {

	@Test
	public void createBody1() {

		Airport airport = new Airport("12", "ARN", "Arlanda");

		Date date = new Date("1992", "03", "31", "12", "00", "00");

		Seat[] seats = new Seat[] {
			new Seat("1", "TJOFRÄS", "1", false, 0, 0, false),
			new Seat("2", "TJOFRÄS", "2", true, 0, 1, false)
		};

		Flight f = new Flight("TJOFRÄS", airport, date, seats, "2");

		String[] expected_body = new String[] {
			"TJOFRÄS",
			"12", "ARN", "Arlanda",
			"1992", "03", "31", "12", "00", "00",
			"1", "TJOFRÄS", "1", "false", "0", "0", "false",
			"2", "TJOFRÄS", "2", "true", "0", "1", "false",
			"2"
		};

		assertArrayEquals(expected_body, f.createBody());
	}
}
