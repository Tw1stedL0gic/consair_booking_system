package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.Mailbox;
import ospp.bookinggui.networking.messsages.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketListener implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketListener.class.getName());

	private final Mailbox<Message>    mailbox;
	private final BufferedInputStream reader;

	public PacketListener(Mailbox<Message> m, InputStream is) {
		this.mailbox = m;
		this.reader = new BufferedInputStream(is);
	}

	@Override
	public void run() {
		byte[] header = new byte[5];
		byte[] message = null;

		int index = 0;
		int data = 0;
		try {
			int message_length = 0;
			short message_id = 0;

			while((data = this.reader.read()) != -1) {
				if(index < 4) {
					header[index] = (byte) (data & 0xFF);
				}
				else if(index == 4) {
					message_length = 0;

					message_length |= header[0] << 24;
					message_length |= header[1] << 16;
					message_length |= header[2] << 8;
					message_length |= header[3];

					message_id = (short) (header[4] & 0xFF);

					message = new byte[message_length];
				}
				else if(index < (message_length - 5)) {
					message[index] = (byte) (data & 0xFF);
				}
				else {
					logger.fine("Finished reading message!");

					mailbox.recieve(Message.deconstructMessage(message_id, message));

					index = -1;
				}

				++index;
			}
		}
		catch(IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
