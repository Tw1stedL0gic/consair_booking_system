package ospp.bookinggui;

import java.util.Arrays;

public class Utils {

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
			ia[i] = ba[i];
		}
		return ia;
	}

	public static byte[] concat(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
