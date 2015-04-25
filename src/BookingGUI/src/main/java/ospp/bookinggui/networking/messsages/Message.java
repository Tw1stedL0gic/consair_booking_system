package ospp.bookinggui.networking.messsages;

public abstract class Message {

	public enum Type {

		GET_PASSENGERS((short) 1),
		GET_PASSENGERS_RESP((short) 2),
		BOOK_SEAT((short) 3),
		BOOK_SEAT_RESP((short) 4),
		LOGIN((short) 5),
		LOGIN_RESP((short) 6),
		DISCONNECT((short) 7),
		HEARTBEAT((short) 8);

		public final short ID;

		private Type(short id) {
			this.ID = id;
		}
	}

	private final Type TYPE;
	private final Integer MESSAGE_LENGTH;
	private final byte[] MESSAGE;

	protected Message(Type t, byte[] m) {
		this.TYPE = t;
		this.MESSAGE_LENGTH = 5 + m.length;
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
		message[4] = (byte) (TYPE.ID & 0xFF);

		for(int i = 5; i < this.MESSAGE_LENGTH; i++) {
			message[i] = this.MESSAGE[i - 5];
		}

		return message;
	}
}
