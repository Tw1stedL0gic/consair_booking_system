package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class HandshakeResponse extends Message {

	private final boolean success;

	public HandshakeResponse(int response) {
		this.type = Type.HANDSHAKE_RESPONSE;
		this.success = ((byte) response) == ((byte) 0xff);
	}

	public boolean isSuccessful() {
		return this.success;
	}

	@Override
	public byte[] constructBody() {
		byte value = this.success ? (byte) 0xff : 0x00;
		return new byte[] {value};
	}
}
