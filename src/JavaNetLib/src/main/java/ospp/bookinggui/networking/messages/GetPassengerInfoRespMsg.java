package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerInfoRespMsg extends Message {
	protected GetPassengerInfoRespMsg() {
		super(Type.GET_PASSENGER_INFO_RESP);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
