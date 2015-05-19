package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class ErrorMessage extends Message {
	public ErrorMessage(String message) {
		super(Type.ERROR, System.currentTimeMillis(), message);
	}
}
