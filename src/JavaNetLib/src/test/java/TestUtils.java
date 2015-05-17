import org.junit.Test;
import ospp.bookinggui.Utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class TestUtils {

	@Test
	public void testConcat() {
		byte[] one = new byte[]{
			1, 2, 3, 4, 5
		};
		byte[] two = new byte[]{
			6, 7, 8, 9, 10
		};

		byte[] con = Utils.concat(one, two);

		// Test length
		assertTrue(one.length + two.length == con.length);

		// Test content
		assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, con);
	}

	@Test
	public void testBytePresentationByteArg() {
		byte[] ba = new byte[]{
			0x12, 0x53, (byte) 0xff, (byte) 0xfa
		};

		String bs = Utils.bytePresentation(ba);

		assertTrue(bs.equals("12 53 ff fa "));
	}

	@Test
	public void testBytePresentationIntArg() {
		int[] ia = new int[]{
			0xff1223fa, 0x3421dadd, 0x33333333, 0xffffffff, 0xf0000000, 0x0f000000
		};

		String is = Utils.bytePresentation(ia);

		assertTrue(is.equals("ff1223fa 3421dadd 33333333 ffffffff f0000000 0f000000 "));
	}
}
