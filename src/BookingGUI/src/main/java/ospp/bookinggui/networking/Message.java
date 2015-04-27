package ospp.bookinggui.networking;

import ospp.bookinggui.networking.messages.Handshake;
import ospp.bookinggui.networking.messages.HandshakeResponse;

import java.util.logging.Logger;

public abstract class Message {

	private static final Logger logger = Logger.getLogger(Message.class.getName());

	private static final int HEADER_SIZE = 5;

	public enum Type {

		HANDSHAKE(1),
		HANDSHAKE_RESPONSE(2),
		GET_PASSENGERS(3),
		GET_PASSENGERS_RESP(4),
		BOOK_SEAT(5),
		BOOK_SEAT_RESP(6),
		DISCONNECT(7),
		HEARTBEAT(8),
		GET_PASSENGER_INFO(9),
		GET_PASSENGER_INFO_RESP(10);

		public final byte ID;

		private Type(int id) {
			this.ID = (byte) (id & 0xFF);
		}

		public static Type getType(int id) {
			switch(id) {
				case 1:
					return Type.HANDSHAKE;
				case 2:
					return Type.HANDSHAKE_RESPONSE;
				case 3:
					return Type.GET_PASSENGERS;
				case 4:
					return Type.GET_PASSENGERS_RESP;
				case 5:
					return Type.BOOK_SEAT;
				case 6:
					return Type.BOOK_SEAT_RESP;
				case 7:
					return Type.DISCONNECT;
				case 8:
					return Type.HEARTBEAT;
				case 9:
					return Type.GET_PASSENGER_INFO;
				case 10:
					return Type.GET_PASSENGER_INFO_RESP;
			}

			return null;
		}
	}

	protected Type type = null;
	protected byte[] message = null;

	/**
	 * Constructs a byte array representation of this message to be sent over the socket.
	 *
	 * @return The byte array representation of this message.
	 */
	public byte[] constructMessage() {

		int message_length = this.message.length + HEADER_SIZE;

		byte[] message = new byte[message_length];

		message[0] = (byte) ((message_length & 0xFF000000) >> 24);
		message[1] = (byte) ((message_length & 0x00FF0000) >> 16);
		message[2] = (byte) ((message_length & 0x0000FF00) >> 8);
		message[3] = (byte)  (message_length & 0x000000FF);
		message[4] = type.ID;

		for(int i = 5; i < message_length; i++) {
			message[i] = this.message[i - 5];
		}

		return message;
	}
	
	public static Message deconstructMessage(short id, byte[] message) {

		Type type = Type.getType(id);
		
		switch(type) {
			case HANDSHAKE:
				int usr_length = 0;
				usr_length |= message[0] << 8;
				usr_length |= message[1];

				byte[] usr = new byte[usr_length];
				System.arraycopy(message, 2, usr, 0, usr_length);
				String username = usr.toString();

				int pas_length = 0;
				pas_length |= message[usr_length] << 8;
				pas_length |= message[usr_length + 1];

				byte[] pas = new byte[pas_length];
				System.arraycopy(message, usr_length + 2, pas, 0, pas_length);
				String password = pas.toString();

				return new Handshake(username, password);

			case HANDSHAKE_RESPONSE:
				return new HandshakeResponse(message[0]);

			default:
				logger.severe("Unsupported message id!");
				logger.severe("ID: "  + id);
		}
		
		return null;
	}
}
