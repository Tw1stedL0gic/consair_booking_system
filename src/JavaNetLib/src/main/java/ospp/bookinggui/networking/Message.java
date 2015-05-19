package ospp.bookinggui.networking;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.messages.DisconnectMsg;
import ospp.bookinggui.networking.messages.ErrorMsg;
import ospp.bookinggui.networking.messages.LoginMsg;
import ospp.bookinggui.networking.messages.LoginRespMsg;

import java.util.logging.Logger;

public class Message {

	/**
	 * This is what separates the different parts of a message.
	 */
	public static final String SEPARATOR = "&";

	/**
	 * This is the least amount of parts needed for each message.
	 * Current parts:
	 * First part:   ID
	 * Second part:  Timestamp (milliseconds)
	 * Third and up: The message body
	 */
	public static final int HEADER_SIZE = 2;

	private static final Logger logger = Logger.getLogger(Message.class.getName());

	private final Type     TYPE;
	private final long     TIMESTAMP;
	private final String[] BODY;

	public Message(Type type, long timestamp, String... body) {
		this.TYPE = type;
		this.TIMESTAMP = timestamp;
		this.BODY = body;
	}

	public static Message parseMessage(String data) throws MalformedMessageException {

		String[] parts = data.split(Message.SEPARATOR);

		if(parts.length < Message.HEADER_SIZE) {
			throw new MalformedMessageException("Could not parse message! The message is too small!");
		}

		Type type;
		try {
			type = Type.getType(Integer.valueOf(parts[0]));
		}
		catch(NumberFormatException e) {
			throw new MalformedMessageException("The message ID is not an integer!");
		}
		catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new MalformedMessageException("The supplied message ID is not supported!");
		}

		long timestamp;
		try {
			timestamp = Long.valueOf(parts[1]);
		}
		catch(NumberFormatException e) {
			throw new MalformedMessageException("The timestamp is not a valid long!");
		}

		String[] body = null;
		if(parts.length > Message.HEADER_SIZE) {
			body = new String[parts.length - Message.HEADER_SIZE];
			System.arraycopy(parts, 2, body, 0, parts.length - Message.HEADER_SIZE);
		}

		try {
			switch(type) {
				case LOGIN:
					return new LoginMsg(timestamp, body[0], body[1]);

				case LOGIN_RESP:
					return new LoginRespMsg(timestamp, body[0]);

				case DISCONNECT:
					return new DisconnectMsg(timestamp);

				case ERROR:
					return new ErrorMsg(timestamp, body[0]);

				case INIT_BOOK:
					break;

				case INIT_BOOK_RESP:
					break;

				case FIN_BOOK:
					break;

				case FIN_BOOK_RESP:
					break;

				case ABORT_BOOK:
					break;

				case REQ_AIRPORTS:
					break;

				case REQ_AIRPORTS_RESP:
					break;

				case SEARCH_ROUTE:
					break;

				case SEARCH_ROUTE_RESP:
					break;

				case REQ_FLIGHT_DETAILS:
					break;

				case REQ_FLIGHT_DETAILS_RESP:
					break;

				case REQ_SEAT_SUGGESTION:
					break;

				case REQ_SEAT_SUGGESTION_RESP:
					break;

				case REQ_SEAT_MAP:
					break;

				case REQ_SEAT_MAP_RESP:
					break;

				case REQ_PASSENGER_LIST:
					break;

				case REQ_PASSENGER_LIST_RESP:
					break;

				case REQ_SEAT_MAP_ADMIN:
					break;

				case REQ_SEAT_MAP_ADMIN_RESP:
					break;

			}
		}
		catch(NullPointerException | IndexOutOfBoundsException e) {
			throw new MalformedMessageException("Could not parse message! The body is not correctly formed!");
		}

		// This line should never be reached!
		throw new MalformedMessageException("Something went horribly wrong during parsing!");
	}

	public String createMessage() {
		StringBuilder message = new StringBuilder();

		message.append(this.TYPE.ID).append(Message.SEPARATOR);
		message.append(this.TIMESTAMP).append(Message.SEPARATOR);

		for(String arg : this.BODY) {
			message.append(arg).append(Message.SEPARATOR);
		}

		return message.toString();
	}

	public Type getType() {
		return this.TYPE;
	}

	public long getTimestamp() {
		return this.TIMESTAMP;
	}

	public String[] getBody() {
		return this.BODY;
	}

	public enum Type {

		/*
		 * IMPORTANT! The order of these types determine the ID of the message!
		 * The first type in the constants below is thus the ID #1 etc.
		 */

		// General types
		LOGIN,
		LOGIN_RESP,
		DISCONNECT,
		ERROR,

		// User commands
		INIT_BOOK,
		INIT_BOOK_RESP,

		FIN_BOOK,
		FIN_BOOK_RESP,

		ABORT_BOOK,

		REQ_AIRPORTS,
		REQ_AIRPORTS_RESP,

		SEARCH_ROUTE,
		SEARCH_ROUTE_RESP,

		REQ_FLIGHT_DETAILS,
		REQ_FLIGHT_DETAILS_RESP,

		REQ_SEAT_SUGGESTION,
		REQ_SEAT_SUGGESTION_RESP,

		REQ_SEAT_MAP,
		REQ_SEAT_MAP_RESP,

		// Admin commands
		REQ_PASSENGER_LIST,
		REQ_PASSENGER_LIST_RESP,

		REQ_SEAT_MAP_ADMIN,
		REQ_SEAT_MAP_ADMIN_RESP;

		public final byte ID;

		Type() {
			this.ID = (byte) ((this.ordinal() + 1) & 0xFF);
		}

		public static Type getType(int id) {
			if(id > Type.values().length) {
				throw new IllegalArgumentException("Incorrect ID! ID is too large!");
			}
			return Type.values()[id - 1];
		}
	}
}