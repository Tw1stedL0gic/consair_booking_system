package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class LoginRespMsg extends Message {
	public LoginRespMsg(long timestamp, String privilege_level) {
		super(MessageType.LOGIN_RESP, timestamp, privilege_level);
	}
}
