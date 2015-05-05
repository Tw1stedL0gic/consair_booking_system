package runnables;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ospp.bookinggui.Mailbox;
import ospp.bookinggui.Utils;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.Handshake;
import ospp.bookinggui.networking.messages.HandshakeResponse;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestPacketListener {

	@Before
	public void setup() {
		Logger logger = Logger.getLogger(PacketListener.class.getName());
		logger.setLevel(Level.OFF);
	}

	@Test(timeout = 500)
	public void testOne() throws UnsupportedEncodingException {
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

	@Test(timeout = 500)
	public void testTwo() throws UnsupportedEncodingException {
		byte[] m1 = new Handshake("tjenare", "greger").createMessage();
		byte[] m2 = new Handshake("poop", "dickbutt").createMessage();

		Mailbox<Message> mailbox = new Mailbox<>();

		ByteArrayInputStream input = new ByteArrayInputStream(Utils.concat(m1, m2));

		PacketListener pl = new PacketListener(mailbox, input);

		pl.run();

		Message one = mailbox.getOldestIncoming();
		Message two = mailbox.getOldestIncoming();
		Message three = mailbox.getOldestIncoming();

		assertTrue(one != null);
		assertTrue(two != null);
		assertTrue(three == null);

		assertTrue(one instanceof Handshake);
		assertTrue(two instanceof Handshake);

		Handshake mOne = (Handshake) one;
		Handshake mTwo = (Handshake) two;

		assertEquals(mOne.getUsername(), "tjenare");
		assertEquals(mOne.getPassword(), "greger");

		assertEquals(mTwo.getUsername(), "poop");
		assertEquals(mTwo.getPassword(), "dickbutt");
	}
}