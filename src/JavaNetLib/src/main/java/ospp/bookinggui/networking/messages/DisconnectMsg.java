package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class DisconnectMsg extends Message {

	public final String username;

	public DisconnectMsg(long timestamp, String username) {
		super(MessageType.DISCONNECT, timestamp, username);
		this.username = username;
	}
}