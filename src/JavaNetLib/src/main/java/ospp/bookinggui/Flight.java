package ospp.bookinggui;

public class Flight {

	public static final int ARG_AMOUNT = 2 + Airport.ARG_AMOUNT + Date.ARG_AMOUNT;

	private final String  FLIGHT_ID;
	private final Airport airport;
	private final Date    date;
	private final String  FLIGHT_NR;

	public Flight(String flight_id, Airport airport, Date date, String flight_nr) {
		this.FLIGHT_ID = flight_id;
		this.airport = airport;
		this.date = date;
		this.FLIGHT_NR = flight_nr;
	}

	public static Flight parseBody(String[] body, int offset) {
		String flight_id = body[offset++];

		Airport airport = Airport.parseBody(body, offset);
		offset += Airport.ARG_AMOUNT;

		Date date = Date.parseBody(body, offset);
		offset += Date.ARG_AMOUNT;

		String flight_nr = body[offset];

		return new Flight(flight_id, airport, date, flight_nr);
	}

	public String getFlightID() {
		return this.FLIGHT_ID;
	}

	public Airport getAirport() {
		return this.airport;
	}

	public Date getDate() {
		return this.date;
	}

	public String getFlightNumber() {
		return this.FLIGHT_NR;
	}

	public String[] createBody() {
		String[] airport_bod = airport.createBody();
		String[] date_bod = date.createBody();

		String[] body = new String[airport_bod.length + date_bod.length + 2];

		int index = 0;

		body[index] = FLIGHT_ID;

		for(String s : airport_bod) {
			body[++index] = s;
		}

		for(String s : date_bod) {
			body[++index] = s;
		}

		body[++index] = FLIGHT_NR;

		return body;
	}
}