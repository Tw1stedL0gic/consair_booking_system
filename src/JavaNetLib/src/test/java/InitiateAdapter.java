import ospp.bookinggui.exceptions.MalformedMessageException;
import ospp.bookinggui.logging.ConsoleFormatter;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitiateAdapter {

	private static final Logger root_logger = Logger.getLogger("");

	public static void main(String[] args) throws IOException {

		for(Handler h : root_logger.getHandlers()) {
			if(h instanceof ConsoleHandler) {
				h.setFormatter(new ConsoleFormatter());
			}
		}

		root_logger.setLevel(Level.ALL);

		PacketListener.debug = true;


		final Mailbox<Message> mailbox = new Mailbox<>();

		NetworkAdapter adapter = new NetworkAdapter(mailbox, "212.25.154.51", 53535);

		Scanner scanner = new Scanner(System.in);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					Message msg = mailbox.getOldestIncoming();

					if(msg == null)
						continue;

					try {
						System.out.println(msg.createMessage());
					}
					catch(UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		String input;
		while(true) {
			input = scanner.next();

			try {
				Message msg = Message.parseMessage(input);
				mailbox.send(msg);
			}
			catch(MalformedMessageException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}
