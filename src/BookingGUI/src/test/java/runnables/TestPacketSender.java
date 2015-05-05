package runnables;

import org.junit.Before;
import org.junit.Test;
import ospp.bookinggui.Mailbox;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeMSG;
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

		HandshakeMSG m = new HandshakeMSG("tjenare", "greger");

		mailbox.send(m);

		// Ugly! But we need to wait for the sender to complete its work
		try {
			Thread.sleep(30);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}

		sender.stop();

		assertArrayEquals(m.createMessage(), output.toByteArray());
	}

	@Test(timeout = 500)
	public void testTwo() throws UnsupportedEncodingException {
		Mailbox<Message> mailbox = new Mailbox<>();
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		PacketSender sender = new PacketSender(mailbox, output);
		new Thread(sender).start();

		HandshakeMSG m1 = new HandshakeMSG("tjenare", "greger");
		HandshakeMSG m2 = new HandshakeMSG("tjosan", "posan");

		mailbox.send(m1);
		mailbox.send(m2);

		try {
			Thread.sleep(50);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}

		sender.stop();

		assertArrayEquals(Utils.concat(m1.createMessage(), m2.createMessage()), output.toByteArray());
	}
}