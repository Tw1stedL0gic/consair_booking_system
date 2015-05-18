package ospp.bookinggui.networking.messages;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class BookSeatRespMsg extends Message {

	private final BookSeatRespType TYPE;

	public BookSeatRespMsg(BookSeatRespType type) {
		super(Type.BOOK_SEAT_RESP);
		this.TYPE = type;
	}

	public static BookSeatRespMsg parse(byte[] body) throws MalformedMessageException {
		switch(body[0]) {
			case 0x00:
				return new BookSeatRespMsg(BookSeatRespType.SUCCESS);
			case 0x10:
				return new BookSeatRespMsg(BookSeatRespType.LOCKED);
			case 0x20:
				return new BookSeatRespMsg(BookSeatRespType.BOOKED);
		}

		throw new MalformedMessageException("The BookSeatRespMsg had an incorrect response type!");
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		//TODO BookSeatRespMsg.constructBody()
		return new byte[0];
	}

	public BookSeatRespType getType() {
		return this.TYPE;
	}

	public enum BookSeatRespType {
		SUCCESS,
		LOCKED,
		BOOKED
	}
}
