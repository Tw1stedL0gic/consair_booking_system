package ospp.bookinggui.networking.runnables;

import java.io.*;

public class PacketSender implements Runnable {

	private final PrintWriter writer;

	public PacketSender(OutputStream os) {
		this.writer = new PrintWriter(new OutputStreamWriter(os));
	}

	@Override
	public void run() {

	}
}
