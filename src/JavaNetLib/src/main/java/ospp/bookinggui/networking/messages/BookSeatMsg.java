package ospp.bookinggui.networking.messages;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Passenger;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class BookSeatMsg extends Message {

	private final BookingInfo info;

	public BookSeatMsg(BookingInfo info) {
		super(Type.BOOK_SEAT);
		this.info = info;
	}

	public static BookSeatMsg parse(byte[] body) {
		//TODO BookSeatMsg.parse()
		return null;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		Flight f = info.getFlight();
		Passenger p = info.getPassenger();
		int s = info.getSeatNumber();

		byte[] fn = f.getFlightNumber().getBytes(Message.ENCODING);

		byte[] seat = new byte[]{
			(byte) ((s & 0xff00) >> 8), (byte) (s & 0xff)
		};

		byte[] ui = Message.createUIBlock(p);

		byte[] body = new byte[Message.AL_SIZE + fn.length + seat.length + ui.length];

		int index = 0;

		Message.setALValue(body, fn.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(body, fn, index);
		index += fn.length;

		Message.setArgument(body, seat, index);
		index += seat.length;

		Message.setArgument(body, ui, index);

		return body;
	}
}