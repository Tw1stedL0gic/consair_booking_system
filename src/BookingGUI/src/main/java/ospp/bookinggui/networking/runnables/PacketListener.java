package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.Mailbox;
import ospp.bookinggui.networking.messsages.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PacketListener implements Runnable {

	private final Mailbox<Message>    mailbox;
	private final BufferedInputStream reader;

	public PacketListener(Mailbox<Message> m, InputStream is) {
		this.mailbox = m;
		this.reader = new BufferedInputStream(is);
	}

	@Override
	public void run() {
		ArrayList<Integer> buffer = new ArrayList<>();

		int index = 0;
		int data = 0;
		try {
			int message_length = 0;

			while((data = this.reader.read()) != -1) {
				buffer.add(data);
				
				if(index++ == 3) {
					message_length |= buffer.get(0) << 24;
					message_length |= buffer.get(1) << 16;
					message_length |= buffer.get(2) << 8;
					message_length |= buffer.get(3);
				}
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
