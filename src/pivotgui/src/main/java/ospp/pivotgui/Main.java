package ospp.pivotgui;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.*;

public class Main implements Application {

	private Window window = null;

	public void startup(Display display, Map<String, String> map) throws Exception {
		this.window = new Window();

		Label l = new Label("FooBar");

		window.setContent(l);
		window.setTitle("FooBar");

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
