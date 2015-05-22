package ospp.bookinggui.networking;

import ospp.bookinggui.networking.runnables.PacketListener;
import ospp.bookinggui.networking.runnables.PacketSender;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class NetworkAdapter {

	public static int so_timeout_millis = 30000;

	private static final Logger logger = Logger.getLogger(NetworkAdapter.class.getName());

	private final Mailbox<Message> mailbox;

	/**
	 * Initiates the network adapter.
	 * It will try to connect to the given host address and port on the calling thread!
	 *
	 * @param box The mailbox this network adapter should work with.
	 * @param host The address of the host.
	 * @param port The port on the host machine to target.
	 * @throws IOException If an IOException occurred when attempting to connect to the host machine.
	 */
	public NetworkAdapter(Mailbox<Message> box, String host, int port) throws IOException {
		this.mailbox = box;

		logger.info("Attempting to connect to host!");

		Socket socket = new Socket(host, port);

		// Set a timeout on read calls.
		socket.setSoTimeout(NetworkAdapter.so_timeout_millis);

		logger.info("Connection established!");

		new Thread(new PacketListener(box, socket.getInputStream()), "PacketListener").start();
		new Thread(new PacketSender(box, socket.getOutputStream()), "PacketSender").start();
	}
}