package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PacketListener implements Runnable {

	private static final Logger logger = Logger.getLogger(PacketListener.class.getName());

	public static volatile boolean debug = false;

	private final Mailbox<Message>    mailbox;
	private final BufferedInputStream input;

	public PacketListener(Mailbox<Message> m, InputStream is) {
		this.mailbox = m;
		this.input = new BufferedInputStream(is);
	}

	@Override
	public void run() {
		int data;

		int m_length = 0;
		int[] message = null;
		short id = 0;

		int index = 0;

		try {
			while((data = this.input.read()) != -1) {

				logger.fine("Recieved data: " + Utils.bytePresentation(new int[] {data}));

				if(!debug) {
					// Header - Message Length
					if(index < Message.HEADER_SIZE - 1) {
						m_length |= (data << (8 * (3 - index)));
					}

					// Header - Message ID
					else if(index == Message.HEADER_SIZE - 1) {
						// We are only interested in storing the body of the message.
						message = new int[m_length - 1];
						id = (short) (data & 0xff);
					}

					// Message body
					else if(index < m_length + Message.HEADER_SIZE - 1) {
						message[index - Message.HEADER_SIZE] = data;

						// Finished, add message to inbox and reset accumulators!
						if(index == m_length + Message.HEADER_SIZE - 2) {
							logger.fine("Added message to inbox! MSG: " + Utils.bytePresentation(message));
							mailbox.recieve(Message.parseMessage(id, message));

							index = -1;
							m_length = 0;
							message = null;
							id = 0;
						}
					}

					// Increment index counter
					++index;
				}
			}

			logger.severe("END OF STREAM!");
		}
		catch(IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}