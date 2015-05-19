package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketSender implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketSender.class.getName());

	private final Mailbox<Message> mailbox;
	private final PrintWriter      output;

	private volatile boolean keepRunning = true;

	public PacketSender(Mailbox<Message> mailbox, OutputStream os) {
		this.mailbox = mailbox;
		this.output = new PrintWriter(os);
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

			try {
				output.println(m.createMessage());
				output.flush();
			}
			catch(UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public void stop() {
		this.keepRunning = false;
	}
}