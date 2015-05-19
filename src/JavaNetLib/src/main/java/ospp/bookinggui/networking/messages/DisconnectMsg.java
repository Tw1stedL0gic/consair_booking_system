package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class DisconnectMsg extends Message {
	public DisconnectMsg(long timestamp) {
		super(Type.DISCONNECT, timestamp);
	}
}
