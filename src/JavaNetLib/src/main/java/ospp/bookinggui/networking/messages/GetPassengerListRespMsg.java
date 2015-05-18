package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class GetPassengerListRespMsg extends Message {

	private final long[] passenger_ids;

	public GetPassengerListRespMsg(long[] ids) {
		super(Type.GET_PASSENGERS_RESP);
		this.passenger_ids = ids;
	}

	public static GetPassengerListRespMsg parse(byte[] body) {
		if(body.length % 8 != 0) {
			throw new IllegalArgumentException("The given body array does not have a length divisible by eight!");
		}

		long[] ids = new long[body.length / 8];

		for(int i = 0; i < body.length; i++) {
			ids[i / 8] = (ids[i / 8] << 8) | body[i];
		}

		return new GetPassengerListRespMsg(ids);
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		byte[] body = new byte[8 * this.passenger_ids.length];

		for(int i = 0; i < passenger_ids.length; i++) {
			long paid = passenger_ids[i];
			byte[] block = Message.createPaidBlock(paid);

			System.arraycopy(block, 0, body, 8 * i, 8);
		}

		return body;
	}

	public long[] getPaidArray() {
		return this.passenger_ids;
	}
}