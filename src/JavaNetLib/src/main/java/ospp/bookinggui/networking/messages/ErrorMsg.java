package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class ErrorMsg extends Message {
	public ErrorMsg(long timestamp, String message) {
		super(Type.ERROR, timestamp, message);
	}
}
