package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.Seat;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.DisconnectMsg;
import ospp.bookinggui.networking.messages.InitBookMsg;
import ospp.bookinggui.networking.messages.InitBookRespMsg;
import ospp.pivotgui.Main;
import ospp.pivotgui.exceptions.DisconnectException;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfirmController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(ConfirmController.class.getName());

	@BXML
	private TableView  seatTable    = null;
	@BXML
	private PushButton selectButton = null;

	private Seat[] seat_list = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {
		addSelectButtonPressListener();
		addTableSelectionListener();

		// Load seats into the table view
		if(seat_list != null) {
			ArrayList<String> table = new ArrayList<>();

			for(Seat s : seat_list) {
				HashMap<String, String> row = new HashMap<>();

				row.put("seatID", s.getSeatID());
				row.put("class", s.getKlass());
				row.put("window", String.valueOf(s.getWindow()));
				row.put("row", String.valueOf(s.getRow()));
				row.put("column", String.valueOf(s.getCol()));
				row.put("locked", String.valueOf(s.isLocked()));
			}

			seatTable.setTableData(table);
		}
	}

	private void addTableSelectionListener() {
		seatTable.getTableViewSelectionListeners().add(new TableViewSelectionListener() {
			@Override
			public void selectedRangeAdded(TableView tableView, int i, int i1) {
			}

			@Override
			public void selectedRangeRemoved(TableView tableView, int i, int i1) {}
			@Override
			public void selectedRangesChanged(TableView tableView, Sequence<Span> sequence) {}

			@Override
			public void selectedRowChanged(TableView tableView, Object o) {
				new Task<InitBookRespMsg.ResponseCode>() {

					@Override
					public InitBookRespMsg.ResponseCode execute() throws TaskExecutionException {

						HashMap<String, String> row = (HashMap<String, String>) seatTable.getSelectedRow();
						String seatID = row.get("seatID");

						Main.mailbox.send(new InitBookMsg(System.currentTimeMillis(), seatID));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof InitBookRespMsg) {
							InitBookRespMsg resp = (InitBookRespMsg) msg;
							return resp.getResponseCode();
						}
						else if(msg instanceof DisconnectMsg) {
							throw new TaskExecutionException(new DisconnectException());
						}
						else {
							throw new TaskExecutionException(new Exception("Client received incorrect message type!"));
						}
					}

				}.execute(new TaskAdapter<>(new TaskListener<InitBookRespMsg.ResponseCode>() {

					@Override
					public void taskExecuted(Task<InitBookRespMsg.ResponseCode> task) {
						InitBookRespMsg.ResponseCode resp = task.getResult();

						switch(resp) {
							case SUCCESS:
								Alert.alert(MessageType.INFO, "Your seat has been locked for you for a timeperiod!", ConfirmController.this);
								break;

							case FAILED_LOCKED:
								Alert.alert(MessageType.ERROR, "That seat was already locked! Please choose another!", ConfirmController.this);
								break;

							case FAILED_BOOKED:
								Alert.alert(MessageType.ERROR, "That seat was already booked! Please choose another!", ConfirmController.this);
								break;

							case FAILED_SEAT_DID_NOT_EXIST:
								Alert.alert(MessageType.ERROR, "That seat did not exist! Pleas choose another!", ConfirmController.this);
								break;
						}
					}

					@Override
					public void executeFailed(Task<InitBookRespMsg.ResponseCode> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);

						if(e instanceof DisconnectException) {
							Main.disconnectError(ConfirmController.this);
						}
						else {
							Alert.alert(MessageType.ERROR, e.getMessage(), ConfirmController.this);
						}
					}
				}));
			}
		});
	}

	private void addSelectButtonPressListener() {
		selectButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {

			}
		});
	}

	public void setSeats(Seat[] seats) {
		this.seat_list = seats;
	}
}
