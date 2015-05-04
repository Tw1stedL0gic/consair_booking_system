package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class HandshakeResponse extends Message {

	private final boolean success;

	public HandshakeResponse(boolean success) {
		this.type = Type.HANDSHAKE_RESPONSE;
		this.success = success;
	}

	public boolean isSuccessful() {
		return this.success;
	}

	@Override
	public byte[] constructBody() {
		byte value = this.success ? (byte) 0x000000ff : 0x00000000;
		return new byte[] {value};
	}
}
