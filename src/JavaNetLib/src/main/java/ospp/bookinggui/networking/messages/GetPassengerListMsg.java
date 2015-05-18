package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerListMsg extends Message {

	private final Flight flight;

	public GetPassengerListMsg(Flight f) {
		super(Type.GET_PASSENGERS);
		this.flight = f;
	}

	public static GetPassengerListMsg parse(byte[] body) throws UnsupportedEncodingException {
		int index = 0;

		int al = Message.getALValue(body, index);
		index += Message.AL_SIZE;

		byte[] fn = new byte[al];

		System.arraycopy(body, index, fn, 0, al);

		Flight f = new Flight(new String(fn, Message.ENCODING));

		return new GetPassengerListMsg(f);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		String f_number = flight.getFlightNumber();
		byte[] f_number_ba = f_number.getBytes(Message.ENCODING);

		byte[] message = new byte[Message.AL_SIZE + f_number_ba.length];

		Message.setALValue(message, f_number_ba.length, 0);
		Message.setArgument(message, f_number_ba, Message.AL_SIZE);

		return message;
	}

	public Flight getFlight() {
		return this.flight;
	}
}
