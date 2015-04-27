package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class HandshakeResponse extends Message {

	private final boolean success;

	public HandshakeResponse(short response) {
		this.message = new byte[] {(byte) (response & 0xFF)};
		this.type = Type.HANDSHAKE_RESPONSE;

		this.success = response == 0xFF;
	}

	public boolean isSuccessfull() {
		return this.success;
	}
}
