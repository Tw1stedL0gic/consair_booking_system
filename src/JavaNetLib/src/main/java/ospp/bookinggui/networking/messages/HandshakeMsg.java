package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class HandshakeMsg extends Message {

	private final String username;
	private final String password;

	public HandshakeMsg(String username, String password) {
		super(Type.HANDSHAKE);
		this.username = username;
		this.password = password;
	}

	public static HandshakeMsg parse(byte[] body) throws UnsupportedEncodingException {
		int index = 0;

		int al_usr = 0;
		al_usr |= body[index++] << 8;
		al_usr |= body[index++];

		byte[] usr = new byte[al_usr];
		System.arraycopy(body, index, usr, 0, al_usr);
		String username = new String(usr, Message.ENCODING);

		index += al_usr;

		int al_pas = 0;
		al_pas |= body[index++] << 8;
		al_pas |= body[index++];

		byte[] pas = new byte[al_pas];
		System.arraycopy(body, index, pas, 0, al_pas);
		String password = new String(pas, Message.ENCODING);

		return new HandshakeMsg(username, password);
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		byte[] usr = username.getBytes(Message.ENCODING);
		byte[] pas = password.getBytes(Message.ENCODING);

		byte[] body = new byte[4 + usr.length + pas.length];

		int index = 0;

		body[index++] = (byte) ((usr.length & 0xFF00) >> 8);
		body[index++] = (byte) (usr.length & 0x00FF);
		System.arraycopy(usr, 0, body, index, usr.length);

		index += usr.length;

		body[index++] = (byte) ((pas.length & 0xFF00) >> 8);
		body[index++] = (byte) (pas.length & 0x00FF);
		System.arraycopy(pas, 0, body, index, pas.length);

		return body;
	}
}