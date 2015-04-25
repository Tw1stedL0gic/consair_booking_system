package ospp.bookinggui.networking.runnables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PacketListener implements Runnable {

	private final BufferedReader reader;

	public PacketListener(InputStream is) {
		this.reader = new BufferedReader(new InputStreamReader(is));
	}

	@Override
	public void run() {

	}
}
