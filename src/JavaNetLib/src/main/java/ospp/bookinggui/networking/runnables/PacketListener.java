package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.ErrorMsg;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketListener implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketListener.class.getName());

	public static volatile boolean debug = false;

	private final Mailbox<Message> mailbox;
	private final BufferedReader   input;

	public PacketListener(Mailbox<Message> m, InputStream is) {
		this.mailbox = m;
		this.input = new BufferedReader(new InputStreamReader(is));
	}

	@Override
	public void run() {

		String data;

		try {
			while((data = this.input.readLine()) != null) {
				if(!debug) {
					try {
						Message msg = Message.parseMessage(data);
						mailbox.receive(msg);
					}
					catch(MalformedMessageException e) {
						logger.log(Level.SEVERE, "PacketListener received malformed message! " +
							"Message: \"" + e.getMessage() + "\"", e);
						mailbox.send(new ErrorMsg(e.getMessage()));
					}
				}
				else {
					logger.info("Received message: " + data);
				}
			}

			logger.severe("END OF STREAM!");
		}
		catch(IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}