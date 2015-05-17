package ospp.bookinggui.networking.runnables;

import ospp.bookinggui.Utils;
import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.ErrorMessage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
		byte[] body = null;
		short id = 0;

		int index = 0;

		try {
			while((data = this.input.read()) != -1) {

				logger.fine("Recieved data: " + Utils.bytePresentation(new int[]{data}));

				if(!debug) {
					// Header - Message Length
					if(index < Message.HEADER_SIZE - 1) {
						m_length |= (data << (8 * (3 - index)));
					}

					// Header - Message ID
					else if(index == Message.HEADER_SIZE - 1) {
						// We are only interested in storing the body of the message.
						body = new byte[m_length - 1];
						id = (short) (data & 0xff);
					}

					// Message body
					else if(index < m_length + Message.HEADER_SIZE - 1) {
						body[index - Message.HEADER_SIZE] = (byte) data;

						// Finished, add message to inbox and reset accumulators!
						if(index == m_length + Message.HEADER_SIZE - 2) {
							logger.fine("Added message to inbox! MSG: " + Utils.bytePresentation(body));

							Message msg;
							try {
								msg = Message.parseMessage(id, body);
								mailbox.recieve(msg);
							}
							catch(UnsupportedEncodingException e) {
								logger.log(Level.SEVERE, e.getMessage(), e);
							}
							catch(MalformedMessageException e) {
								logger.log(Level.SEVERE, e.getMessage(), e);
								mailbox.send(new ErrorMessage("The received message was malformed!"));
							}

							index = -1;
							m_length = 0;
							body = null;
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