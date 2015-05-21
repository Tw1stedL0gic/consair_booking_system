package ospp.bookinggui;

import ospp.bookinggui.networking.Message;

public class Flight {

	public static final int ARG_AMOUNT = 2 + Airport.ARG_AMOUNT + Date.ARG_AMOUNT + Seat.ARG_AMOUNT;

	private final String  FLIGHT_ID;
	private final Airport airport;
	private final Date    date;
	private final Seat[]  seat_list;
	private final String  FLIGHT_NR;

	public Flight(String flight_id, Airport airport, Date date, Seat[] seat_list, String flight_nr) {
		this.FLIGHT_ID = flight_id;
		this.airport = airport;
		this.date = date;
		this.seat_list = seat_list;
		this.FLIGHT_NR = flight_nr;
	}

	public static Flight parseBody(String[] body, int offset) {
		String flight_id = body[offset++];

		Airport airport = Airport.parseBody(body, offset)[0];
		offset += Airport.ARG_AMOUNT;

		Date date = Date.parseBody(body, offset);
		offset += Date.ARG_AMOUNT;


	}

	public String getFlightID() {
		return this.FLIGHT_ID;
	}

	public Airport getAirport() {
		return this.airport;
	}

	public Date getDate(){
		return this.date;
	}

	public Seat[] getSeatList() {
		return this.seat_list.clone();
	}

	public String getFlightNumber() {
		return this.FLIGHT_NR;
	}

	public String[] createBody() {
		String[] airport_bod = airport.createBody();
		String[] date_bod = date.createBody();

		int seat_fields = 0;

		String[][] seats = new String[seat_list.length][];
		for(int i = 0; i < seat_list.length; i++) {
			seats[i] = seat_list[i].createBody();
			seat_fields += seats[i].length;
		}

		String[] body = new String[airport_bod.length + date_bod.length + seat_fields + 2];

		int index = 0;

		body[index] = FLIGHT_ID;

		for(String s : airport_bod) {
			body[++index] = s;
		}

		for(String s : date_bod) {
			body[++index] = s;
		}

		for(String[] sa : seats) {
			for(String s : sa) {
				body[++index] = s;
			}
		}

		body[++index] = FLIGHT_NR;

		return body;
	}
}
