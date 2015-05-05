package ospp.bookinggui.networking.messages;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class BookSeatMSG extends Message {

	public BookSeatMSG(Flight f, BookingInfo info) {
		this.type = Type.BOOK_SEAT;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}