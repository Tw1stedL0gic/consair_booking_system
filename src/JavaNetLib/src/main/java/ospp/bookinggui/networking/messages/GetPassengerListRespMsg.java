package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerListRespMsg extends Message {
	public GetPassengerListRespMsg() {
		super(Type.GET_PASSENGERS_RESP);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
