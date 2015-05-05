package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerListMSG extends Message {

	private final Flight flight;

	public GetPassengerListMSG(Flight f) {
		this.type = Type.GET_PASSENGERS;
		this.flight = f;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return new byte[0];
	}
}
