package ospp.bookinggui;

import ospp.bookinggui.networking.Adapter;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.Message;

public class Main {

	public static void main(String[] args) {
		//TODO start WORKING YOU FOOL!

		Mailbox<Message> mailbox = new Mailbox<>();

		Adapter adapter = new NetworkAdapter(mailbox);

		//TODO initiate networkadapter
		//TODO start GUI loop
	}
}
