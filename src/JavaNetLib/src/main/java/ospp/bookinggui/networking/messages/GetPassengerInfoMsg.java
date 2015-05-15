package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Passenger;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerInfoMsg extends Message {

	private final Passenger passenger;

	public GetPassengerInfoMsg(Passenger p) {
		super(Type.GET_PASSENGER_INFO);
		this.passenger = p;
	}

	public static GetPassengerInfoMsg parse(int[] body) {
		//TODO GetPassengerInfoMsg.parse()
		return null;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		byte[] msg = new byte[8];

		Long id = this.passenger.getIdentification();

		msg[0] = (byte) (((id & 0xff00000000000000L) >> 56) & 0xff);
		msg[1] = (byte) ((id & 0x00ff000000000000L) >> 48);
		msg[2] = (byte) ((id & 0x0000ff0000000000L) >> 40);
		msg[3] = (byte) ((id & 0x000000ff00000000L) >> 32);
		msg[4] = (byte) ((id & 0x00000000ff000000L) >> 24);
		msg[5] = (byte) ((id & 0x0000000000ff0000L) >> 16);
		msg[6] = (byte) ((id & 0x000000000000ff00L) >> 8);
		msg[7] = (byte) (id & 0x00000000000000ffL);

		return msg;
	}
}