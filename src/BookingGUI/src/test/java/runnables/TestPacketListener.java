package runnables;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ospp.bookinggui.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.HandshakeResponse;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class TestPacketListener {

	@Test(timeout = 500)
	public void testOne() throws UnsupportedEncodingException {
		Logger logger = Logger.getLogger(PacketListener.class.getName());
		logger.setLevel(Level.OFF);

		byte[] ba = new HandshakeResponse(true).createMessage();
		Mailbox<Message> mailbox = new Mailbox<>();

		ByteArrayInputStream input = new ByteArrayInputStream(ba);

		PacketListener pl = new PacketListener(mailbox, input);

		// This can trigger a test timeout if the runnable doesnt terminate correctly.
		pl.run();

		Message m = mailbox.getOldestIncoming();
		Message m2 = mailbox.getOldestOutgoing();

		assertTrue(m2 == null);
		assertTrue(m != null);
		assertTrue(m instanceof HandshakeResponse);

		HandshakeResponse hr = (HandshakeResponse) m;

		assertTrue(hr.isSuccessful());
	}
}
