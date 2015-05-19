import ospp.bookinggui.logging.ConsoleFormatter;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.IOException;
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


		Mailbox<Message> mailbox = new Mailbox<>();

		NetworkAdapter adapter = new NetworkAdapter(mailbox, "localhost", 3333);

		while(true) {

		}
	}
}
