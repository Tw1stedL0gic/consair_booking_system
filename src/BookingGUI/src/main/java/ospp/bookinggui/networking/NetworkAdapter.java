package ospp.bookinggui.networking;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Mailbox;
import ospp.bookinggui.PAID;
import ospp.bookinggui.networking.messsages.Message;

public class NetworkAdapter implements Adapter {

	private final Mailbox<Message> mailbox;

	public NetworkAdapter(Mailbox<Message> box) {
		this.mailbox = box;
	}

	@Override
	public void getPassengerList(Flight flight) {

	}

	@Override
	public void getFlightList() {

	}

	@Override
	public void login(String username, String password) {

	}

	@Override
	public void connect(String url, int port) {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public void book(Flight flight, BookingInfo booking) {

	}

	@Override
	public void getPassengerInfo(PAID id) {

	}
}
