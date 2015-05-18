package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class GetFlightListRespMsg extends Message {

	private final ArrayList<Flight> flight_list;

	public GetFlightListRespMsg(ArrayList<Flight> flight_list) {
		super(Type.GET_FLIGHT_LIST_RESP);
		this.flight_list = flight_list;
	}

	public static GetFlightListRespMsg parse(byte[] body) throws UnsupportedEncodingException {
		ArrayList<Flight> flights = new ArrayList<>();

		for(int i = 0; i < body.length; ) {
			int al = Message.getALValue(body, i);
			i += Message.AL_SIZE;

			byte[] arg = Message.getArgument(body, al, i);
			i += al;

			Flight f = new Flight(new String(arg, Message.ENCODING));

			flights.add(f);
		}

		return new GetFlightListRespMsg(flights);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		//TODO finish GetFlightRespMsg constructBody()
		return new byte[0];
	}

	public ArrayList<Flight> getFlightList() {
		return this.flight_list;
	}
}
