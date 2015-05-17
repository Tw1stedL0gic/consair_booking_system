package ospp.bookinggui.networking.messages;

import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class ErrorMessage extends Message {

	private final String message;

	public ErrorMessage(String msg) {
		super(Type.ERROR);
		this.message = msg;
	}

	public static ErrorMessage parse(int[] body) throws UnsupportedEncodingException {
		byte[] converted_body = Utils.convertIntArrayToByte(body);
		return new ErrorMessage(new String(converted_body, Message.ENCODING));
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return this.message.getBytes(Message.ENCODING);
	}
}
