package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class DisconnectMSG extends Message {

	public DisconnectMSG() {
		this.type = Type.DISCONNECT;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
