package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

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
	public byte[] constructBody() {
		byte[] body = new byte[Message.AL_SIZE * 2 + username.length() + password.length()];

		int i = 0;

		body[i++] = (byte) ((username.length() & 0xFF00) >> 8);
		body[i++] = (byte) (username.length() & 0x00FF);

		for(byte b : username.getBytes()) {
			body[i++] = b;
		}

		body[i++] = (byte) ((password.length() & 0xFF00) >> 8);
		body[i++] = (byte) (password.length() & 0x00FF);

		for(byte b : password.getBytes()) {
			body[i++] = b;
		}

		return body;
	}
}
