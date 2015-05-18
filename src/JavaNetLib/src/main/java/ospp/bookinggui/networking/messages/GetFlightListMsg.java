package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetFlightListMsg extends Message {

	public GetFlightListMsg() {
		super(Type.GET_FLIGHT_LIST);
	}

	public static GetFlightListMsg parse(byte[] body) {
		//TODO GetFlightListMsg.parse()
		return null;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
