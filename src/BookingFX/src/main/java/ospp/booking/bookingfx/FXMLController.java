package ospp.booking.bookingfx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLController implements Initializable, ControlledScreen {


	@FXML
	private AnchorPane rootPane;

	@FXML
	private Label label;

	@FXML
	private TextArea textarea;

	@FXML
	private TextField ipField;

	@FXML
	private TextField portField;

	@FXML
	private ProgressIndicator loadingIndicator;

	@FXML
	private Button button;


	private boolean connected = false;
	private ScreenMaster myScreenMaster;

	@FXML
	private void handleButtonAction(ActionEvent event) {
		loadingIndicator.visibleProperty().set(true);

		//loadingIndicator.setVisible(true);
		final Mailbox<Message> mailbox = myScreenMaster.getMailbox();
		ipField.setDisable(true);
		portField.setDisable(true);
		button.setDisable(true);
		// if portField valed
		int port = Integer.valueOf(portField.getText());
		String ip = ipField.getText();
		NetworkAdapter adapter;

		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		timeline.setAutoReverse(true);

		EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				myScreenMaster.setScreen("welcome");
			}
		};

		timeline.getKeyFrames().addAll
			(new KeyFrame(Duration.ZERO,
					new KeyValue(rootPane.opacityProperty(), 1)),
				new KeyFrame(new Duration(3000), onFinished,
					new KeyValue(rootPane.opacityProperty(), 0)));

		timeline.play();

		try {
			adapter = new NetworkAdapter(mailbox, ip, port);


		}
		catch(IOException ex) {
			textarea.setText(ex.getMessage());
			Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
		}

//        mailbox.send(new HandshakeMsg("foo", "bar"));
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
		loadingIndicator.setVisible(false);
	}

	@Override
	public void setScreenParent(ScreenMaster sm) {
		this.myScreenMaster = sm;
	}

	@Override
	public void onScreen() {
	}

	@Override
	public void offScreen() {
	}
}
