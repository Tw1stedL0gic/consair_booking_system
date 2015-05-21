package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Airport;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class RequestAirportsRespMsg extends Message {

	private final Airport[] airports;

	public RequestAirportsRespMsg(long timestamp, String[] body) {
		super(MessageType.REQ_AIRPORTS_RESP, timestamp, body);
		this.airports = parseBody(body);
	}

	public Airport[] getAirports() {
		return this.airports.clone();
	}

	public static String[] createBody(Airport[] airports) {

		String[] body = new String[airports.length * 3];

		for(int i = 0; i < airports.length; i++) {
			Airport port = airports[i];

			body[i * 3] = port.getAirportID();
			body[i * 3 + 1] = port.getIATA();
			body[i * 3 + 2] = port.getName();
		}

		return body;
	}

	public static Airport[] parseBody(String[] body) {

		if(body.length % 3 != 0) {
			throw new IllegalArgumentException("The length of the body is not divisible by three!");
		}

		Airport[] ports = new Airport[body.length / 3];

		for(int i = 0; i < ports.length; i++) {
			String id = body[i * 3];
			String iata = body[i * 3 + 1];
			String name = body[i * 3 + 2];

			ports[i] = new Airport(id, iata, name);
		}

		return ports;
	}
}
