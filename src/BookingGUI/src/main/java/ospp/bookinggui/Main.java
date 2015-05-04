package ospp.bookinggui;

import ospp.bookinggui.networking.Adapter;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.Message;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		//TODO start WORKING YOU FOOL!

		if(args.length != 2) {
			throw new IllegalArgumentException("You need to specify host and portnumber!");
		}

		Mailbox<Message> mailbox = new Mailbox<>();

		Adapter adapter = new NetworkAdapter(mailbox, args[0], Integer.valueOf(args[1]));

		//TODO initiate networkadapter
		//TODO start GUI loop
	}
}
