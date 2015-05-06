package ospp.bookinggui;

public class BookingInfo {

	private final Passenger passenger;
	private final Flight    flight;
	private final Seat      seat;

	public BookingInfo(Passenger p, Flight f, Seat s) {
		this.passenger = p;
		this.flight = f;
		this.seat = s;
	}

	public Passenger getPassenger() {
		return this.passenger;
	}

	public Flight getFlight() {
		return this.flight;
	}

	public Seat getSeat() {
		return this.seat;
	}
}