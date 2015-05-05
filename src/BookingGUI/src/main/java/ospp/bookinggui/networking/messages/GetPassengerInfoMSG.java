package ospp.bookinggui.networking.messages;

import ospp.bookinggui.PassengerIdentification;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerInfoMSG extends Message {

	public GetPassengerInfoMSG(PassengerIdentification id) {
		this.type = Type.GET_PASSENGER_INFO;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
