package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class BookSeatRespMsg extends Message {
	protected BookSeatRespMsg() {
		super(Type.BOOK_SEAT_RESP);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
