package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class ErrorMsg extends Message {
	public ErrorMsg(long timestamp, String message) {
		super(MessageType.ERROR, timestamp, message);
	}
}
