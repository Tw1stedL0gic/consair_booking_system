package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class InitBookMsg extends Message {

	private final String flight_id;
	private final String seat_id;

	public InitBookMsg(long timestamp, String flight_id, String seat_id) {
		super(MessageType.INIT_BOOK, timestamp, flight_id, seat_id);
		this.flight_id = flight_id;
		this.seat_id = seat_id;
	}

	public String getFlightID() {
		return this.flight_id;
	}

	public String getSeatID() {
		return this.seat_id;
	}
}