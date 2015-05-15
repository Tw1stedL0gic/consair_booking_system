package messages;

import org.junit.Test;
import ospp.bookinggui.networking.messages.GetPassengerListRespMsg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetPassengerListResp {

	@Test
	public void parse() {
		byte[] body1 = new byte[] {
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08
		};

		GetPassengerListRespMsg msg1 = GetPassengerListRespMsg.parse(body1);

		long[] pa1 = msg1.getPaidArray();

		assertTrue(pa1.length == 1);

		long paid1 = pa1[0];

		assertEquals(0x0102030405060708L, paid1);

		byte[] body2 = new byte[] {
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
			0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18
		};

		GetPassengerListRespMsg msg2 = GetPassengerListRespMsg.parse(body2);

		long[] pa2 = msg2.getPaidArray();

		assertTrue(pa2.length == 2);

		long paid21 = pa2[0];
		long paid22 = pa2[1];

		assertEquals(0x0102030405060708L, paid21);
		assertEquals(0x1112131415161718L, paid22);
	}

	@Test
	public void constructBody() {

	}
}