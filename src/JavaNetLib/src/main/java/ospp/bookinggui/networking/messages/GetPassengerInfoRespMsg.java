package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerInfoRespMsg extends Message {
	public GetPassengerInfoRespMsg() {
		super(Type.GET_PASSENGER_INFO_RESP);
	}

	public static GetPassengerInfoRespMsg parse(int[] body) {
		//TODO GetPassengerInfoRespMsg.parse()
		return null;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		//TODO GetPassengerInfoRespMsg.constructBody()
		return new byte[0];
	}
}
