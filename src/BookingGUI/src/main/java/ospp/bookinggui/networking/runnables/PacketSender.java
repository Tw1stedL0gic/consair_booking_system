package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.Mailbox;
import ospp.bookinggui.networking.Message;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketSender implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketSender.class.getName());

	private final Mailbox<Message> mailbox;
	private final BufferedOutputStream output;

	public PacketSender(Mailbox<Message> mailbox, OutputStream os) {
		this.mailbox = mailbox;
		this.output = new BufferedOutputStream(os);
	}

	@Override
	public void run() {

		while(true) {
			Message m = mailbox.getOldestOutgoing();

			// If mailbox is empty, sleep for a while then check again.
			if(m == null) {
				try {
					Thread.sleep(100);
					continue;
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Attempt to create message.
			byte[] data;
			try {
				data = m.createMessage();
			}
			catch(UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				continue;
			}

			// Attempt to send the data.
			try {
				this.output.write(data, 0, data.length);
			}
			catch(IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}