package ospp.bookinggui;

public class Flight {

	private final String FLIGHT_NUMBER;

	public Flight(String flight_number) {
		this.FLIGHT_NUMBER = flight_number;
	}

	public String getFlightNumber() {
		return this.FLIGHT_NUMBER;
	}
}