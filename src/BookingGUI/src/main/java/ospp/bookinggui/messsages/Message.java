package ospp.bookinggui.messsages;

public abstract class Message {

	public enum Type {

		GET_PASSENGERS((byte) 1),
		GET_PASSENGERS_RESP((byte) 2),
		BOOK_SEAT((byte) 3),
		BOOK_SEAT_RESP((byte) 4),
		LOGIN((byte) 5),
		LOGIN_RESP((byte) 6),
		DISCONNECT((byte) 7),
		HEARTBEAT((byte) 8);

		public final byte ID;

		private Type(byte id) {
			this.ID = id;
		}
	}

	private final Type TYPE;
	private final byte MESSAGE_LENGTH;
	private final byte[] MESSAGE;

	protected Message(Type t, byte[] m) {
		this.TYPE = t;
		this.MESSAGE_LENGTH = (byte) (2 + m.length);
		this.MESSAGE = m;
	}

	/**
	 * Constructs a byte array representation of this message to be sent over the socket.
	 *
	 * @return The byte array representation of this message.
	 */
	public byte[] constructMessage() {
		byte[] message = new byte[this.MESSAGE_LENGTH];

		message[0] = this.TYPE.ID;
		message[1] = this.MESSAGE_LENGTH;

		for(int i = 2; i < this.MESSAGE_LENGTH; i++) {
			message[i] = this.MESSAGE[i - 2];
		}

		return message;
	}
}
