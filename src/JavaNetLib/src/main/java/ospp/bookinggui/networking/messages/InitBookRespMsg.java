package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class InitBookRespMsg extends Message {

	public final String response_code;

	public InitBookRespMsg(long timestamp, String resp_code) {
		super(MessageType.INIT_BOOK_RESP, timestamp, resp_code);
		this.response_code = resp_code;
	}
}