package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Passenger;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerInfoRespMsg extends Message {

	private final Passenger passenger;

	public GetPassengerInfoRespMsg(Passenger p) {
		super(Type.GET_PASSENGER_INFO_RESP);
		this.passenger = p;
	}

	public static GetPassengerInfoRespMsg parse(int[] b) throws UnsupportedEncodingException {
		//TODO Fix the line below!!
		byte[] body = Utils.convertIntArrayToByte(b);

		int index = 0;

		// PAID
		byte[] paid_arg = Message.getArgument(body, 8, index);
		index += 8;

		long paid = 0;
		for(int i = 0; i < paid_arg.length; i++) {
			paid = (paid << 8) | paid_arg[i];
		}

		// PN
		int pn_al = Message.getALValue(body, index);
		index += Message.AL_SIZE;

		byte[] pn_arg = Message.getArgument(body, pn_al, index);
		index += pn_al;

		String pn = new String(pn_arg, Message.ENCODING);

		// ADR
		int adr_al = Message.getALValue(body, index);
		index += Message.AL_SIZE;

		byte[] adr_arg = Message.getArgument(body, adr_al, index);
		index += adr_al;

		String adr = new String(adr_arg, Message.ENCODING);

		// PI
		int pi_al = Message.getALValue(body, index);
		index += Message.AL_SIZE;

		byte[] pi_arg = Message.getArgument(body, pi_al, index);
		index += pi_al;

		String pi = new String(pi_arg, Message.ENCODING);

		// EMAIL
		int email_al = Message.getALValue(body, index);
		index += Message.AL_SIZE;

		byte[] email_arg = Message.getArgument(body, email_al, index);
		index += email_al;

		String email = new String(email_arg, Message.ENCODING);

		// Finished reading, construct passenger object and return.

		//TODO Fix this object initialization to use what we actually extracted from the packet!
		Passenger p = new Passenger(paid, pn, adr, pi, email);

		return new GetPassengerInfoRespMsg(p);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		//TODO GetPassengerInfoRespMsg.constructBody()
		return new byte[0];
	}

	public Passenger getPassenger() {
		return this.passenger;
	}
}