package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class LoginMsg extends Message {

	public LoginMsg(long timestamp, String username, String password) {
		super(Type.LOGIN, timestamp, username, password);
	}
}
