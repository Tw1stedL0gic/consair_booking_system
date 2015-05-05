package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetFlightListMSG extends Message {

	public GetFlightListMSG() {
		this.type = Type.GET_FLIGHT_LIST;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
