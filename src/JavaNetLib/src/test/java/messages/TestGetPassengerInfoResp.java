package messages;

import org.junit.Test;
import ospp.bookinggui.Passenger;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.GetPassengerInfoRespMsg;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestGetPassengerInfoResp {

	@Test
	public void parse1() throws UnsupportedEncodingException {
		byte[] id = new byte[]{
			0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17
		};

		byte[] pn = "Tjenare".getBytes(Message.ENCODING);

		byte[] adr = "TestVägen12".getBytes(Message.ENCODING);

		byte[] pi = "Betalkort".getBytes(Message.ENCODING);

		byte[] email = "test@example.com".getBytes(Message.ENCODING);

		byte[] msg_array = new byte[Message.AL_SIZE * 4 + id.length + pn.length + adr.length + pi.length + email.length];

		int index = 0;

		Message.setArgument(msg_array, id, index);
		index += 8;

		Message.setALValue(msg_array, pn.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(msg_array, pn, index);
		index += pn.length;

		Message.setALValue(msg_array, adr.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(msg_array, adr, index);
		index += adr.length;

		Message.setALValue(msg_array, pi.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(msg_array, pi, index);
		index += pi.length;

		Message.setALValue(msg_array, email.length, index);
		index += Message.AL_SIZE;

		Message.setArgument(msg_array, email, index);


		GetPassengerInfoRespMsg msg = GetPassengerInfoRespMsg.parse(msg_array);

		Passenger p = msg.getPassenger();

		assertTrue(p != null);

		assertEquals(0x1011121314151617L, p.getIdentification());
		assertEquals("Tjenare", p.getName());
		assertEquals("TestVägen12", p.getAddress());
		assertEquals("Betalkort", p.getPaymentInfo());
		assertEquals("test@example.com", p.getEmail());
	}

	@Test
	public void parse2() throws UnsupportedEncodingException {
		byte[] msg_array = new byte[]{
			// PAID
			0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,

			// PN
			0x00, 0x07,
			0x74, 0x6A, 0x65, 0x6E, 0x61, 0x72, 0x65, // "tjenare" hex encoded

			// ADR
			0x00, 0x06,
			0x66, 0x6F, 0x6F, 0x62, 0x61, 0x72, // "foobar"

			// PI
			0x00, 0x06,
			0x62, 0x61, 0x72, 0x66, 0x6F, 0x6F, // "barfoo"

			// EMAIL
			0x00, 0x0b,
			0x66, 0x6F, 0x6F, 0x40, 0x62, 0x61, 0x72, 0x2E, 0x63, 0x6F, 0x6D, // "foo@bar.com"
		};

		GetPassengerInfoRespMsg msg = GetPassengerInfoRespMsg.parse(msg_array);

		Passenger p = msg.getPassenger();

		assertTrue(p != null);

		assertEquals("tjenare", p.getName());
		assertEquals("foobar", p.getAddress());
		assertEquals("barfoo", p.getPaymentInfo());
		assertEquals("foo@bar.com", p.getEmail());
	}

	@Test
	public void constructBody() {

	}
}
