package ospp.pivotgui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.pivotgui.controllers.LoginController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main implements Application {

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static final Mailbox<Message> mailbox = new Mailbox<Message>();

	private Window main_window = null;
	private Display display = null;
	private PushButton connect_button = null;

	private ActivityIndicator indicator = null;

	public void startup(final Display display, Map<String, String> map) throws Exception {
		this.display = display;

		BXMLSerializer bxmlSerializer = new BXMLSerializer();

		main_window = (Window) bxmlSerializer.readObject(Main.class, "/bxml/main.bxml");

		final Map<String, Object> namespace = bxmlSerializer.getNamespace();

		connect_button = (PushButton) namespace.get("connectButton");
		indicator = (ActivityIndicator) namespace.get("indicator");

		// Add event-listener, attempt to connect to the server.
		connect_button.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {

				TextInput url_in = (TextInput) namespace.get("url");
				TextInput port_int = (TextInput) namespace.get("port");

				final String url = url_in.getText();
				final int port = Integer.valueOf(port_int.getText());

				activateIndicator(connect_button, indicator);

				new Task<Void>() {
					@Override
					public Void execute() throws TaskExecutionException {
						// Attempt to establish a connection.

						if(url.equals("test")) {
							return null;
						}

						try {
							new NetworkAdapter(mailbox, url, port);
						}
						catch(IOException e) {
							throw new TaskExecutionException(e);
						}

						return null;
					}
				}.execute(new TaskAdapter<Void>(new TaskListener<Void>() {
					@Override
					public void taskExecuted(Task<Void> task) {
						disableIndicator(connect_button, indicator);
						continueToLogin();
					}

					@Override
					public void executeFailed(Task<Void> task) {
						disableIndicator(connect_button, indicator);

						// Display and log error
						Throwable e = task.getFault();
						Alert.alert(MessageType.ERROR, "Could not establish a connection!", main_window);
						logger.log(Level.WARNING, e.getMessage(), e);
					}
				}));
			}
		});

		// Open the window.
		main_window.open(display);
	}

	private void continueToLogin() {
		// A connection has been established! WOHOO!
		// Close the main window, parse login.bxml and open it.
		main_window.close();
		BXMLSerializer bxml = new BXMLSerializer();
		try {
			Window login = (Window) bxml.readObject(LoginController.class, "/bxml/login.bxml");
			login.open(display);
		}
		catch(IOException | SerializationException e) {
			e.printStackTrace();
		}
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
		if(this.main_window != null) {
			this.main_window.close();
		}

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
