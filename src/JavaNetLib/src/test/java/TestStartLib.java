import org.junit.Test;
import ospp.bookinggui.networking.Adapter;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;

import java.io.IOException;

public class TestStartLib {

	@Test
	public void start() throws IOException {
		Mailbox<Message> mailbox = new Mailbox<>();
		Adapter adapter = new NetworkAdapter(mailbox, "130.243.180.55", 3333);

		adapter.disconnect();
	}
}
