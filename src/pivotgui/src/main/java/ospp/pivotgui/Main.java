package ospp.pivotgui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class Main implements Application {

	private Window window = null;

	public void startup(Display display, Map<String, String> map) throws Exception {
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		window = (Window) bxmlSerializer.readObject(Main.class, "/bxml/hello.bxml");
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
