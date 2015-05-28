package ospp.pivotgui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.*;

public class Main implements Application {

	public final Mailbox<Message> = new Mailbox<Message>();
	private Window window = null;

	public void startup(Display display, Map<String, String> map) throws Exception {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();

		// Load main.bxml stylesheet into a Window-object
		window = (Window) bxmlSerializer.readObject(Main.class, "/bxml/main.bxml");

		// Retrieve the namespace of the loaded bxml file.
		Map<String, Object> namespace = bxmlSerializer.getNamespace();

		// Retrieve the button defined in the bxml file with the bxml:id="connectButton"
		PushButton b = (PushButton) namespace.get("connectButton");

		// Add event-listener, display message on button press.
		b.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				Alert.alert(MessageType.QUESTION, "TjosanHejsan!", window);
			}
		});

		// Open the window.
		window.open(display);
	}

	public boolean shutdown(boolean b) throws Exception {
		if(this.window != null) {
			this.window.close();
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
