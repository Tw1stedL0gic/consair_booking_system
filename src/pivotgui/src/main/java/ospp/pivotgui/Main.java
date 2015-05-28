package ospp.pivotgui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.*;

public class Main implements Application {

	private Window window = null;

	public void startup(Display display, Map<String, String> map) throws Exception {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		window = (Window) bxmlSerializer.readObject(Main.class, "/bxml/main.bxml");

		Map<String, Object> namespace = bxmlSerializer.getNamespace();

		PushButton b = (PushButton) namespace.get("connectButton");

		b.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				Alert.alert(MessageType.QUESTION, "TjosanHejsan!", window);
			}
		});

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
