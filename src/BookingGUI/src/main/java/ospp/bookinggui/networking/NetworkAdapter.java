package ospp.bookinggui.networking;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Mailbox;
import ospp.bookinggui.PAID;
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

	}

	@Override
	public void getFlightList() {

	}

	@Override
	public void login(String username, String password) {

	}

	@Override
	public void connect(String url, int port) {
		//TODO send handshake to server
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
