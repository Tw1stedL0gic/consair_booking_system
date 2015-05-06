package ospp.bookinggui.networking;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Mailbox;
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
	private final Socket socket;

	public NetworkAdapter(Mailbox<Message> box, String host, int port) throws IOException {
		this.mailbox = box;

		logger.info("Attempting to connect to host!");

		this.socket = new Socket(host, port);

		logger.info("Connection established!");

		new Thread(new PacketListener(box, this.socket.getInputStream()), "PacketListener").start();
		new Thread(new PacketSender(box, this.socket.getOutputStream()), "PacketSender").start();
	}

	@Override
	public void getPassengerList(Flight flight) {
		mailbox.send(new GetPassengerListMSG(flight));
	}

	@Override
	public void getFlightList() {
		mailbox.send(new GetFlightListMSG());
	}

	@Override
	public void login(String username, String password) {
		mailbox.send(new HandshakeMSG(username, password));
	}

	@Override
	public void disconnect() {
		mailbox.send(new DisconnectMSG());
	}

	@Override
	public void book(Flight flight, BookingInfo booking) {
		mailbox.send(new BookSeatMSG(flight, booking));
	}

	@Override
	public void getPassengerInfo(Passenger id) {
		mailbox.send(new GetPassengerInfoMSG(id));
	}
}