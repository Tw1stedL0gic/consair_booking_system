package ospp.bookinggui.networking;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Passenger;
import ospp.bookinggui.networking.messages.*;
import ospp.bookinggui.networking.runnables.PacketListener;
import ospp.bookinggui.networking.runnables.PacketSender;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkAdapter implements Adapter {

	private static final Logger logger = Logger.getLogger(NetworkAdapter.class.getName());

	private final Mailbox<Message> mailbox;

	public NetworkAdapter(Mailbox<Message> box, String host, int port) throws IOException {
		this.mailbox = box;

		logger.info("Attempting to connect to host!");

		Socket socket = new Socket(host, port);

		logger.info("Connection established!");

		new Thread(new PacketListener(box, socket.getInputStream()), "PacketListener").start();
		new Thread(new PacketSender(box, socket.getOutputStream()), "PacketSender").start();
	}

	@Override
	public void getPassengerList(Flight flight) {
		mailbox.send(new GetPassengerListMsg(flight));
	}

	@Override
	public void getFlightList() {
		mailbox.send(new GetFlightListMsg());
	}

	@Override
	public void login(String username, String password) {
		mailbox.send(new HandshakeMsg(username, password));
	}

	@Override
	public void disconnect() {
		mailbox.send(new DisconnectMsg());
	}

	@Override
	public void book(BookingInfo booking) {
		mailbox.send(new BookSeatMsg(booking));
	}

	@Override
	public void getPassengerInfo(Passenger id) {
		mailbox.send(new GetPassengerInfoMsg(id));
	}
}