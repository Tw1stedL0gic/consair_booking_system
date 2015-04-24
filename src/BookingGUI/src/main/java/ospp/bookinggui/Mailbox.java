package ospp.bookinggui;

import ospp.bookinggui.messsages.Message;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Mailbox {

	private final ConcurrentLinkedQueue<Message> inbox = new ConcurrentLinkedQueue<>();
	private final ConcurrentLinkedQueue<Message> outbox = new ConcurrentLinkedQueue<>();

	/**
	 * Adds the given message to the end of the inbox queue.
	 *
	 * @param m The message to add to the inbox.
	 */
	public void addInbox(Message m) {
		inbox.add(m);
	}

	/**
	 * Adds the given message to the end of the outbox queue.
	 *
	 * @param m The message to add to the outbox.
	 */
	public void addOutbox(Message m) {
		outbox.add(m);
	}

	/**
	 * Polls the head of the inbox and returns it.
	 *
	 * @return The polled message, null if inbox is empty.
	 */
	public Message pollInbox() {
		return inbox.poll();
	}

	/**
	 * Polls the head of the outbox and returns it.
	 *
	 * @return The polled message, null if outbox is empty.
	 */
	public Message pollOutbox() {
		return outbox.poll();
	}
}
