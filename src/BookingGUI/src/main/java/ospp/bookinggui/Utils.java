package ospp.bookinggui;

import java.util.Arrays;

public class Utils {

	private static final char[] ca = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	};

	public static byte[] convertIntArrayToByte(int[] ia) {
		byte[] ba = new byte[ia.length];
		for(int i = 0; i < ia.length; i++) {
			ba[i] = (byte) (ia[i] & 0xff);
		}
		return ba;
	}

	public static int[] convertByteArrayToInt(byte[] ba) {
		int[] ia = new int[ba.length];
		for(int i = 0; i < ba.length; i++) {
			ia[i] = (((int) ba[i]) & 0x000000ff);
		}
		return ia;
	}

	public static byte[] concat(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static String bytePresentation(byte[] ba) {
		StringBuilder sb = new StringBuilder();

		for(byte b : ba) {
			sb.append(ca[((b & 0xf0) >> 4)]);
			sb.append(ca[(b & 0x0f)]);
			sb.append(' ');
		}

		return sb.toString();
	}

	public static String bytePresentation(int[] ia) {
		StringBuilder sb = new StringBuilder();

		for(int i : ia) {
			sb.append(ca[(((i & 0xf0000000) >> 28) & 0x0f)]);
			sb.append(ca[ ((i & 0x0f000000) >> 24)]);
			sb.append(ca[ ((i & 0x00f00000) >> 20)]);
			sb.append(ca[ ((i & 0x000f0000) >> 16)]);
			sb.append(ca[ ((i & 0x0000f000) >> 12)]);
			sb.append(ca[ ((i & 0x00000f00) >> 8)]);
			sb.append(ca[ ((i & 0x000000f0) >> 4)]);
			sb.append(ca[  (i & 0x0000000f)]);
			sb.append(' ');
		}

		return sb.toString();
	}
}