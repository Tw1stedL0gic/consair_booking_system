package ospp.pivotgui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.pivotgui.controllers.ConnectAndLoginController;

import java.io.IOException;
import java.util.logging.Logger;

public class Main implements Application {

	private static final Logger logger = Logger.getLogger(Main.class.getName());
	public static final Mailbox<Message> mailbox = new Mailbox<Message>();
	public static Display display      = null;

	public void startup(final Display display, Map<String, String> map) throws Exception {
		this.display = display;
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		Window login_window = (Window) bxmlSerializer.readObject(ConnectAndLoginController.class, "/bxml/book.bxml");
		login_window.open(display);
	}

	public static void activateIndicator(PushButton b, ActivityIndicator i) {
		b.setButtonData("Please wait...");
		b.setEnabled(false);
		i.setActive(true);
	}

	public static void disableIndicator(PushButton b, ActivityIndicator i) {
		b.setButtonData("Connect");
		b.setEnabled(true);
		i.setActive(false);
	}

	public boolean shutdown(boolean b) throws Exception {
		return false;
	}

	public void suspend() throws Exception {

	}

	public void resume() throws Exception {

	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(Main.class, args);
	}
}
