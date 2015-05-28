package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.ErrorMsg;
import ospp.bookinggui.networking.messages.LoginMsg;
import ospp.bookinggui.networking.messages.LoginRespMsg;
import ospp.pivotgui.Main;
import sun.rmi.runtime.Log;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());

	@BXML
	private TextInput username = null;

	@BXML
	private TextInput password = null;

	@BXML
	private PushButton login = null;

	@BXML
	private ActivityIndicator indicator = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

		login.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				Main.activateIndicator(login, indicator);

				// Send a login message
				Main.mailbox.send(new LoginMsg(System.currentTimeMillis(), username.getText(), password.getText()));

				// Wait for an answer on an async thread.
				new Task<LoginRespMsg.PrivilegeLevel>() {
					@Override
					public LoginRespMsg.PrivilegeLevel execute() throws TaskExecutionException {

						// Wait for answer from server
						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof LoginRespMsg) {
							LoginRespMsg resp = (LoginRespMsg) msg;
							return resp.getPrivilegeLevel();
						}
						else if(msg instanceof ErrorMsg) {
							throw new TaskExecutionException(new Exception(((ErrorMsg) msg).getErrorMessage()));
						}
						else {
							throw new TaskExecutionException(new Exception("Received message was not of correct type!"));
						}
					}
				}.execute(new TaskAdapter<LoginRespMsg.PrivilegeLevel>(new TaskListener<LoginRespMsg.PrivilegeLevel>() {
					@Override
					public void taskExecuted(Task<LoginRespMsg.PrivilegeLevel> task) {
						Main.disableIndicator(login, indicator);
						loadSelectionWindow();
					}

					@Override
					public void executeFailed(Task<LoginRespMsg.PrivilegeLevel> task) {
						Main.disableIndicator(login, indicator);

						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);
						Alert.alert(MessageType.ERROR, e.getMessage(), LoginController.this);
					}
				}));
			}
		});
	}

	private void loadSelectionWindow() {
		this.close();
	}
}
