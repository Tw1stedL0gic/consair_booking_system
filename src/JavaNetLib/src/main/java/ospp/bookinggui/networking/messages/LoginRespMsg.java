package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class LoginRespMsg extends Message {

	public final String privilege_level;

	public LoginRespMsg(long timestamp, String privilege_level) {
		super(MessageType.LOGIN_RESP, timestamp, privilege_level);
		this.privilege_level = privilege_level;
	}
}