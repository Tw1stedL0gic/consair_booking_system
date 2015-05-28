package ospp.pivotgui.buttons;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.*;

import java.net.URL;

public class ConnectButton extends Window implements Bindable {

	private PushButton button;

	@Override
	public void initialize(Map<String, Object> namespace, URL url, Resources resources) {
		button = (PushButton) namespace.get("connectButton");

		button.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				Alert.alert(MessageType.INFO, "Tjosan hejsan!", ConnectButton.this);
			}
		});
	}
}
