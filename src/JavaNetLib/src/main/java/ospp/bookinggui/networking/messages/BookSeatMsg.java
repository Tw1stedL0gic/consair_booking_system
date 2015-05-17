package ospp.bookinggui.networking.messages;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class BookSeatMsg extends Message {

	public BookSeatMsg(Flight f, BookingInfo info) {
		super(Type.BOOK_SEAT);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}

	public static BookSeatMsg parse(byte[] body) {
		//TODO BookSeatMsg.parse()
		return null;
	}
}