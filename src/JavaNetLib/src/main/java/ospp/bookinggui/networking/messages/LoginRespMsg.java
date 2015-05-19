package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class LoginRespMsg extends Message {
	public LoginRespMsg(long timestamp, String privilege_level) {
		super(Type.LOGIN_RESP, timestamp, privilege_level);
	}
}
