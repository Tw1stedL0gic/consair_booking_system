package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class HandshakeMessage extends Message {

	public HandshakeMessage(String username, String password) {

		this.message_length = 2 + username.length() + 2 + password.length();
		this.type = Type.HANDSHAKE;

		byte[] message = new byte[this.message_length];

		int i = 0;

		message[i++] = (byte) ((username.length() & 0xFF00) >> 8);
		message[i++] = (byte) (username.length() & 0x00FF);

		for(byte b : username.getBytes()) {
			message[i++] = b;
		}

		message[i++] = (byte) ((password.length() & 0xFF00) >> 8);
		message[i++] = (byte) (password.length() & 0x00FF);

		for(byte b : password.getBytes()) {
			message[i++] = b;
		}

		this.message = message;
	}
}
