package ospp.bookinggui;

public class BookingInfo {

	private final Passenger passenger;
	private final Flight    flight;
	private final int       SEAT;

	public BookingInfo(Passenger p, Flight f, int seat_number) {
		this.passenger = p;
		this.flight = f;
		this.SEAT = seat_number;
	}

	public Passenger getPassenger() {
		return this.passenger;
	}

	public Flight getFlight() {
		return this.flight;
	}

	public int getSeatNumber() {
		return this.SEAT;
	}
}