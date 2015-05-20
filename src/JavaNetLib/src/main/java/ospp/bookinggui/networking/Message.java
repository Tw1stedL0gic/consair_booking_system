package ospp.bookinggui.networking;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.messages.DisconnectMsg;
import ospp.bookinggui.networking.messages.ErrorMsg;
import ospp.bookinggui.networking.messages.LoginMsg;
import ospp.bookinggui.networking.messages.LoginRespMsg;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

	private final MessageType TYPE;
	private final long        TIMESTAMP;
	private final String[]    BODY;

	/**
	 * Creates a message with the given parameters.
	 *
	 * WARNING! If this constructor is called manually and not from
	 * a child message constructor, you need to make absolutely sure that the way you call
	 * it complies with the defined protocol!
	 *
	 * @param type The type of the message.
	 * @param timestamp The timestamp of the message.
	 * @param body The body of the message.
	 */
	public Message(MessageType type, long timestamp, String... body) {
		this.TYPE = type;
		this.TIMESTAMP = timestamp;
		this.BODY = body;
	}

	/**
	 * Attempts to parse the given string according to the defined protocol.
	 * Throws MalformedMessageException with a description of the problem if any error occurs during parsing.
	 *
	 * @param data The string to parse to a message.
	 * @return The message if successful.
	 * @throws MalformedMessageException If any problems occur during parsing.
	 */
	public static Message parseMessage(String data) throws MalformedMessageException, UnsupportedEncodingException {

		String[] parts = data.split(Message.SEPARATOR);

		// Remove the URL encoding from the message, we no longer need it.
		for(int i = 0; i < parts.length; i++) {
			parts[i] = URLDecoder.decode(parts[i], "UTF8");
		}

		if(parts.length < Message.HEADER_SIZE) {
			throw new MalformedMessageException("Could not parse message! The message is too small!");
		}

		MessageType type;
		try {
			type = MessageType.getType(Integer.valueOf(parts[0]));
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
					return new DisconnectMsg(timestamp, body[0]);

				case ERROR:
					return new ErrorMsg(timestamp, body[0]);

				case INIT_BOOK:
				case FIN_BOOK:
				case FIN_BOOK_RESP:
				case ABORT_BOOK:
				case REQ_AIRPORTS:
				case REQ_AIRPORTS_RESP:
				case SEARCH_ROUTE:
				case SEARCH_ROUTE_RESP:
				case REQ_FLIGHT_DETAILS:
				case REQ_FLIGHT_DETAILS_RESP:
				case REQ_SEAT_SUGGESTION:
				case REQ_SEAT_SUGGESTION_RESP:
				case REQ_SEAT_MAP:
				case REQ_SEAT_MAP_RESP:
				case REQ_PASSENGER_LIST:
				case REQ_PASSENGER_LIST_RESP:
				case REQ_SEAT_MAP_ADMIN:
				case REQ_SEAT_MAP_ADMIN_RESP:
				default:
					logger.warning("Message.parseMessage() is missing specific parsing for the type: " + type);
					return new Message(type, timestamp, body);
			}
		}
		catch(NullPointerException | IndexOutOfBoundsException e) {
			throw new MalformedMessageException("Could not parse message! The body is not correctly formed!");
		}
	}

	/**
	 * Creates the message.
	 *
	 * @return The created message.
	 */
	public String createMessage() throws UnsupportedEncodingException {
		StringBuilder message = new StringBuilder();

		message.append(this.TYPE.ID).append(Message.SEPARATOR);
		message.append(this.TIMESTAMP).append(Message.SEPARATOR);

		for(String arg : this.BODY) {
			message.append(URLEncoder.encode(arg, "UTF8")).append(Message.SEPARATOR);
		}

		return message.toString();
	}

	public MessageType getType() {
		return this.TYPE;
	}

	public long getTimestamp() {
		return this.TIMESTAMP;
	}

	public String[] getBody() {
		return this.BODY;
	}
}