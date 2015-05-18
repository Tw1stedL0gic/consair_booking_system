package ospp.bookinggui.networking;

import ospp.bookinggui.Passenger;
import ospp.bookinggui.Utils;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.messages.*;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Message {

	public static final  int    HEADER_SIZE = 5;
	public static final  String ENCODING    = "UTF8";
	public static final  int    AL_SIZE     = 2;
	private static final Logger logger      = Logger.getLogger(Message.class.getName());
	private final Type type;

	protected Message(Type t) {
		this.type = t;
	}

	public static Message parseMessage(short id, byte[] body) throws UnsupportedEncodingException, MalformedMessageException {

		Type type;
		try {
			type = Type.getType(id);
		}
		catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			throw new MalformedMessageException("The supplied message ID is not supported!");
		}

		try {
			switch(type) {
				case HANDSHAKE:
					return HandshakeMsg.parse(body);

				case HANDSHAKE_RESPONSE:
					return HandshakeRespMsg.parse(body);

				case GET_PASSENGERS:
					return GetPassengerListMsg.parse(body);

				case GET_PASSENGERS_RESP:
					return GetPassengerListRespMsg.parse(body);

				case BOOK_SEAT:
					return BookSeatMsg.parse(body);

				case BOOK_SEAT_RESP:
					return BookSeatRespMsg.parse(body);

				case DISCONNECT:
					return new DisconnectMsg();

				case HEARTBEAT:
					return new HeartbeatMsg();

				case GET_PASSENGER_INFO:
					return GetPassengerInfoMsg.parse(body);

				case GET_PASSENGER_INFO_RESP:
					return GetPassengerInfoRespMsg.parse(body);

				case GET_FLIGHT_LIST:
					return GetFlightListMsg.parse(body);

				case GET_FLIGHT_LIST_RESP:
					return GetFlightListRespMsg.parse(body);

				case ERROR:
					return ErrorMessage.parse(body);

				default:
					logger.severe("Unsupported message id!");
					logger.severe("ID: " + id);
					throw new IllegalArgumentException("Unsupported message id!");
			}
		}
		catch(UnsupportedEncodingException e) {
			throw e;
		}
		catch(Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new MalformedMessageException(
				"Error while attempting to parse message type=\"" + type + "\"! Error message: \"" + e.getMessage() + "\"");
		}
	}

	public static byte[] setALValue(byte[] m, int al, int offset) {
		if(m.length + offset < Message.AL_SIZE) {
			throw new IndexOutOfBoundsException("The message-array can not fit an AL-block!");
		}

		for(int i = 0; i < AL_SIZE; i++) {
			int shift_mult = AL_SIZE - i - 1;
			int shift_amount = 8 * shift_mult;
			int shifted = al >> shift_amount;
			m[offset + i] = (byte) (shifted & 0xff);
		}

		return m;
	}

	public static int getALValue(byte[] m, int offset) {
		if(m.length < offset + Message.AL_SIZE) {
			throw new IndexOutOfBoundsException("There does not fit an AL-block at the given offset in the byte array!");
		}

		int value = 0;

		for(int i = 0; i < Message.AL_SIZE; i++) {
			value = ((value << 8) | m[offset + i]);
		}

		return value;
	}

	public static byte[] setArgument(byte[] out, byte[] in, int offset) {
		if(out.length + offset < in.length) {
			throw new IndexOutOfBoundsException("The in-array is larger than the out-array + offset!");
		}

		System.arraycopy(in, 0, out, offset, in.length);

		return out;
	}

	public static byte[] getArgument(byte[] m, int al, int offset) {
		byte[] arg = new byte[al];
		System.arraycopy(m, offset, arg, 0, al);
		return arg;
	}

	public static byte[] createPaidBlock(long paid) {
		byte[] block = new byte[8];

		for(int i = 0; i < 8; i++) {
			block[i] = (byte) ((paid >> (8 * (8 - i - 1))) & 0xff);
		}

		return block;
	}

	public static byte[] createUIBlock(Passenger p) throws UnsupportedEncodingException {

		byte[] id = Message.createPaidBlock(p.getIdentification());
		byte[] pn = p.getName().getBytes(Message.ENCODING);
		byte[] adr = p.getAddress().getBytes(Message.ENCODING);
		byte[] pi = p.getPaymentInfo().getBytes(Message.ENCODING);
		byte[] email = p.getEmail().getBytes(Message.ENCODING);

		byte[] paid_block = new byte[Message.AL_SIZE * 4 + id.length + pn.length + adr.length + pi.length + email.length];

		int index = 0;

		Message.setArgument(paid_block, id, index);
		index += 8;

		Message.setALValue(paid_block, pn.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(paid_block, pn, index);
		index += pn.length;

		Message.setALValue(paid_block, adr.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(paid_block, adr, index);
		index += adr.length;

		Message.setALValue(paid_block, pi.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(paid_block, pi, index);
		index += pi.length;

		Message.setALValue(paid_block, email.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(paid_block, email, index);

		return paid_block;
	}

	public byte[] constructHeader(int body_size) {
		byte[] header = new byte[HEADER_SIZE];

		int message_length = 1 + body_size;

		header[0] = (byte) ((message_length & 0xff000000) >> 24);
		header[1] = (byte) ((message_length & 0x00ff0000) >> 16);
		header[2] = (byte) ((message_length & 0x0000ff00) >> 8);
		header[3] = (byte) (message_length & 0x000000ff);

		header[4] = type.ID;

		return header;
	}

	public abstract byte[] constructBody() throws UnsupportedEncodingException;

	public byte[] createMessage() throws UnsupportedEncodingException {
		byte[] body = this.constructBody();
		byte[] header = this.constructHeader(body.length);

		return Utils.concat(header, body);
	}

	public enum Type {

		HANDSHAKE,
		HANDSHAKE_RESPONSE,
		HEARTBEAT,
		GET_PASSENGERS,
		GET_PASSENGERS_RESP,
		BOOK_SEAT,
		BOOK_SEAT_RESP,
		DISCONNECT,
		GET_PASSENGER_INFO,
		GET_PASSENGER_INFO_RESP,
		GET_FLIGHT_LIST,
		GET_FLIGHT_LIST_RESP,
		ERROR;

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