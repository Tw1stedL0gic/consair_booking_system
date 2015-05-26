package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Airport;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;

public class SearchAirportRouteMsg extends Message {

	private final Airport from;
	private final Airport to;

	public SearchAirportRouteMsg(long timestamp, String[] body) {
		super(MessageType.SEARCH_ROUTE, timestamp, body);
		this.from = Airport.parseBody(body, 0);
		this.to = Airport.parseBody(body, Airport.ARG_AMOUNT);
	}

	public static String[] constructBody(Airport from, Airport to) {
		String[] body = new String[Airport.ARG_AMOUNT * 2];

		System.arraycopy(from.createBody(), 0, body, 0, Airport.ARG_AMOUNT);
		System.arraycopy(to.createBody(), 0, body, Airport.ARG_AMOUNT, Airport.ARG_AMOUNT);

		return body;
	}

	public Airport getFrom() {
		return this.from;
	}

	public Airport getTo() {
		return this.to;
	}
}
