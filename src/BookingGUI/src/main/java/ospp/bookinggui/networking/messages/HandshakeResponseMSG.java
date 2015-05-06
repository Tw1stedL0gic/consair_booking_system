package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

public class HandshakeResponseMSG extends Message {

	private final boolean success;

	public HandshakeResponseMSG(boolean success) {
		this.type = Type.HANDSHAKE_RESPONSE;
		this.success = success;
	}

	public boolean isSuccessful() {
		return this.success;
	}

	@Override
	public byte[] constructBody() {
		byte value = this.success ? (byte) 0xff : 0x00;
		return new byte[] {value};
	}

	public static HandshakeResponseMSG parse(int[] body) {
		boolean success = body[0] == 0x000000ff;
		return new HandshakeResponseMSG(success);
	}
}