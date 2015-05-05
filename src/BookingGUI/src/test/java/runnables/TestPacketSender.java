package runnables;

import org.junit.Before;
import org.junit.Test;
import ospp.bookinggui.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.Handshake;
import ospp.bookinggui.networking.runnables.PacketSender;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;

public class TestPacketSender {

	@Before
	public void setup() {
		Logger logger = Logger.getLogger(PacketSender.class.getName());
		logger.setLevel(Level.OFF);
	}

	@Test(timeout = 500)
	public void testOne() throws UnsupportedEncodingException {
		Mailbox<Message> mailbox = new Mailbox<>();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		PacketSender sender = new PacketSender(mailbox, output);
		new Thread(sender).start();

		Handshake m = new Handshake("tjenare", "greger");

		mailbox.send(m);

		// Ugly! But we need to wait for the sender to complete its work
		try {
			Thread.sleep(100);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}

		sender.stop();

		assertArrayEquals(m.createMessage(), output.toByteArray());
	}
}