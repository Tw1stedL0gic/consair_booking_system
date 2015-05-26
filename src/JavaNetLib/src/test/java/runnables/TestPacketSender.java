package runnables;

import org.junit.Test;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.runnables.PacketSender;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class TestPacketSender {

	@Test(timeout = 500)
	public void testOne() throws UnsupportedEncodingException {
		Mailbox<Message> mailbox = new Mailbox<>();

		ByteArrayOutputStream output = new ByteArrayOutputStream();

		long timestamp = System.currentTimeMillis();

		mailbox.send(new Message(MessageType.LOGIN, timestamp, "yo", "lo"));

		PacketSender sender = new PacketSender(mailbox, output);
		new Thread(sender).start();

		// Wait for the PacketSender to do its job
		try {
			Thread.sleep(100);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}

		// Stop the sender runnable, killing the thread.
		sender.stop();

		String sent = output.toString();

		// WARNING! This doesn't work on windows!
		// Windows uses the "\r\n" line break char instead of the standard UNIX "\n"
		// "Fuck you! Thats why!" -- Bill Gates
		String expected = "1&" + timestamp + "&yo&lo&\n";

		assertEquals(expected, sent);
	}
}