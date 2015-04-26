package ospp.bookinggui.networking.messsages;

import java.util.logging.Logger;

public abstract class Message {

	private static final Logger logger = Logger.getLogger(Message.class.getName());

	public enum Type {

		GET_PASSENGERS((short) 1),
		GET_PASSENGERS_RESP((short) 2),
		BOOK_SEAT((short) 3),
		BOOK_SEAT_RESP((short) 4),
		LOGIN((short) 5),
		LOGIN_RESP((short) 6),
		DISCONNECT((short) 7),
		HEARTBEAT((short) 8);

		public final byte ID;

		private Type(short id) {
			this.ID = (byte) (id & 0xFF);
		}
	}

	private final Type TYPE;
	private final int MESSAGE_LENGTH;
	private final byte[] MESSAGE;

	protected Message(Type t, byte[] m) {
		this.TYPE = t;
		this.MESSAGE_LENGTH = 5 + (m == null ? 0 : m.length);
		this.MESSAGE = m;
	}

	/**
	 * Constructs a byte array representation of this message to be sent over the socket.
	 *
	 * @return The byte array representation of this message.
	 */
	public byte[] constructMessage() {
		byte[] message = new byte[this.MESSAGE_LENGTH];

		message[0] = (byte) ((this.MESSAGE_LENGTH & 0xFF000000) >> 24);
		message[1] = (byte) ((this.MESSAGE_LENGTH & 0x00FF0000) >> 16);
		message[2] = (byte) ((this.MESSAGE_LENGTH & 0x0000FF00) >> 8);
		message[3] = (byte)  (this.MESSAGE_LENGTH & 0x000000FF);
		message[4] = TYPE.ID;

		for(int i = 5; i < this.MESSAGE_LENGTH; i++) {
			message[i] = this.MESSAGE[i - 5];
		}

		return message;
	}
	
	public static Message deconstructMessage(short id, byte[] message) {
		
		switch(id) {
			case 1:
				
			default:
				logger.severe("Could not deconstruct message!");
				logger.severe("ID: "  + id);
		}
		
		return null;
	}
}
