package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketSender implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketSender.class.getName());

	private final Mailbox<Message>     mailbox;
	private final BufferedOutputStream output;

	private volatile boolean keepRunning = true;

	public PacketSender(Mailbox<Message> mailbox, OutputStream os) {
		this.mailbox = mailbox;
		this.output = new BufferedOutputStream(os);
	}

	@Override
	public void run() {

		while(keepRunning) {
			Message m = mailbox.getOldestOutgoing();

			// If mailbox is empty, sleep for a while then check again.
			if(m == null) {
				try {
					Thread.sleep(10);
					continue;
				}
				catch(InterruptedException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
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
				this.output.flush();
			}
			catch(IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void stop() {
		this.keepRunning = false;
	}
}