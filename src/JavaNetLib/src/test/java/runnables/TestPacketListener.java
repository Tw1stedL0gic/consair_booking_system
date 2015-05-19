package runnables;

import org.junit.Test;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.MessageType;
import ospp.bookinggui.networking.runnables.PacketListener;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestPacketListener {

	@Test(timeout = 500)
	public void testOne() {
		ByteArrayInputStream input = new ByteArrayInputStream("1&1337&yo&lo&".getBytes());

		Mailbox<Message> mailbox = new Mailbox<>();
		PacketListener pl = new PacketListener(mailbox, input);

		assertEquals(null, mailbox.getOldestIncoming());

		pl.run();

		Message msg = mailbox.getOldestIncoming();

		assertNotEquals(null, msg);

		assertEquals(MessageType.LOGIN, msg.getType());
		assertEquals(1337L, msg.getTimestamp());

		assertArrayEquals(new String[]{"yo", "lo"}, msg.getBody());
	}
}