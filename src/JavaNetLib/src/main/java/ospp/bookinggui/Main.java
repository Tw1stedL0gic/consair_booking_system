package ospp.bookinggui;

import ospp.bookinggui.logging.ConsoleFormatter;
import ospp.bookinggui.networking.Adapter;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	private static final Logger root_logger = Logger.getLogger("");

	public static void main(String[] args) throws IOException {
		//TODO start WORKING YOU FOOL!

		root_logger.setLevel(Level.ALL);

		for(Handler h : root_logger.getHandlers()) {
			if(h instanceof ConsoleHandler) {
				h.setFormatter(new ConsoleFormatter());
			}
		}

		if(args.length != 2) {
			throw new IllegalArgumentException("You need to specify host and portnumber!");
		}

		Mailbox<Message> mailbox = new Mailbox<>();

		Adapter adapter = new NetworkAdapter(mailbox, args[0], Integer.valueOf(args[1]));

		//TODO start GUI loop
	}
}