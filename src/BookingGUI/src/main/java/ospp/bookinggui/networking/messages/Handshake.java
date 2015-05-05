package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class Handshake extends Message {

	private final String username;
	private final String password;

	public Handshake(String username, String password) {
		this.username = username;
		this.password = password;
		this.type = Type.HANDSHAKE;
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