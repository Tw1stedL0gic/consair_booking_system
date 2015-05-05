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
					Thread.sleep(20);
					continue;
				}
				catch(InterruptedException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}

			System.out.println("1");

			// Attempt to create message.
			byte[] data;
			try {
				data = m.createMessage();
			}
			catch(UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				continue;
			}

			System.out.println("2");

			// Attempt to send the data.
			try {
				this.output.write(data, 0, data.length);
				System.out.println("3");
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