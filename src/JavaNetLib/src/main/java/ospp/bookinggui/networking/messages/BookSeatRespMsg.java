package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class BookSeatRespMsg extends Message {
	public BookSeatRespMsg() {
		super(Type.BOOK_SEAT_RESP);
	}

	public static BookSeatRespMsg parse(byte[] body) {
		//TODO BookSeatRespMsg.parse()
		return null;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
