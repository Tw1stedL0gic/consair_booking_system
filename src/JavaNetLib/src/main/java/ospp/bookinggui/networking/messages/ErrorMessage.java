package ospp.bookinggui.networking.messages;

import ospp.bookinggui.networking.Message;

import java.io.UnsupportedEncodingException;

public class ErrorMessage extends Message {

	private final String message;

	public ErrorMessage(String msg) {
		super(Type.ERROR);
		this.message = msg;
	}

	public static ErrorMessage parse(byte[] body) throws UnsupportedEncodingException {
		return new ErrorMessage(new String(body, Message.ENCODING));
	}

	@Override
	public byte[] constructBody() throws UnsupportedEncodingException {
		return this.message.getBytes(Message.ENCODING);
	}
}
