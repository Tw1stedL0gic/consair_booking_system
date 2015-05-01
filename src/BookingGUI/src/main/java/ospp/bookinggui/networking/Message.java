package ospp.bookinggui.networking;

import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.messages.Handshake;
import ospp.bookinggui.networking.messages.HandshakeResponse;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public abstract class Message {

	public static final  int    HEADER_SIZE = 5;
	public static final  String ENCODING    = "UTF8";
	public static final  int    AL_SIZE     = 2;
	private static final Logger logger      = Logger.getLogger(Message.class.getName());
	protected            Type   type        = null;

	public static Message parseMessage(short id, int[] message, String encoding) throws UnsupportedEncodingException {

		Type type = Type.getType(id);

		switch(type) {
			case HANDSHAKE:
				return parseHandshake(message, encoding);

			case HANDSHAKE_RESPONSE:
				return new HandshakeResponse(message[0]);

			default:
				logger.severe("Unsupported message id!");
				logger.severe("ID: " + id);
		}

		return null;
	}

	private static Message parseHandshake(int[] message, String encoding) throws UnsupportedEncodingException {
		byte[] m_byte = Utils.convertIntArrayToByte(message);

		int index = 0;

		int al_usr = 0;
		al_usr |= m_byte[index++] << 8;
		al_usr |= m_byte[index++];

		byte[] usr = new byte[al_usr];
		System.arraycopy(m_byte, index, usr, 0, al_usr);
		String username = new String(usr, encoding);

		index += al_usr;

		int al_pas = 0;
		al_pas |= m_byte[index++] << 8;
		al_pas |= m_byte[index++];

		byte[] pas = new byte[al_pas];
		System.arraycopy(m_byte, index, pas, 0, al_pas);
		String password = new String(pas, encoding);

		return new Handshake(username, password);
	}

	public static byte[] setALValue(byte[] m, int al, int offset) {
		for(int i = 0; i < AL_SIZE; i++) {
			m[offset + i] = (byte) ((al >> (8 * (AL_SIZE - i))) & 0xff);
		}
		return m;
	}

	public byte[] constructHeader(int body_size) {
		byte[] header = new byte[HEADER_SIZE];

		int message_length = HEADER_SIZE + body_size;

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
		GET_PASSENGERS,
		GET_PASSENGERS_RESP,
		BOOK_SEAT,
		BOOK_SEAT_RESP,
		DISCONNECT,
		HEARTBEAT,
		GET_PASSENGER_INFO,
		GET_PASSENGER_INFO_RESP;

		public final byte ID;

		private Type() {
			this.ID = (byte) ((this.ordinal() + 1) & 0xFF);
		}

		public static Type getType(int id) {
			return Type.values()[id - 1];
		}
	}
}