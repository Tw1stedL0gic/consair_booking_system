package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.serialization.SerializationException;
import org.apache.pivot.util.Resources;
import org.apache.pivot.util.concurrent.Task;
import org.apache.pivot.util.concurrent.TaskExecutionException;
import org.apache.pivot.util.concurrent.TaskListener;
import org.apache.pivot.wtk.*;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Flight;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.messages.RequestAirportsMsg;
import ospp.bookinggui.networking.messages.RequestAirportsRespMsg;
import ospp.bookinggui.networking.messages.SearchAirportRouteMsg;
import ospp.bookinggui.networking.messages.SearchAirportRouteRespMsg;
import ospp.pivotgui.Main;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SelectionController extends Window implements Bindable {

	private static final Logger logger = Logger.getLogger(SelectionController.class.getName());

	@BXML
	private ListView   fromList     = null;
	@BXML
	private ListView   toList       = null;
	@BXML
	private PushButton selectButton = null;
	@BXML
	private PushButton reloadButton = null;

	private ActivityIndicator sheetIndicator = new ActivityIndicator(){{
		this.setActive(true);
	}};
	private Sheet             sheet          = new Sheet(sheetIndicator);

	private final HashMap<String, Airport> loaded_airports = new HashMap<>();

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

		reloadButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {

//				openIndicator();

				new Task<Airport[]>() {
					@Override
					public Airport[] execute() throws TaskExecutionException {
						Main.mailbox.send(new RequestAirportsMsg(System.currentTimeMillis(), null));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof RequestAirportsRespMsg) {
							RequestAirportsRespMsg resp = (RequestAirportsRespMsg) msg;
							return resp.getAirports();
						}
						else {
							logger.severe("Client received incorrect response type to RequestAirportsMsg! " +
								"Received type: " + msg.getType());
							throw new TaskExecutionException("Client received incorrect response type!");
						}
					}
				}.execute(new TaskAdapter<>(new TaskListener<Airport[]>() {
					@Override
					public void taskExecuted(Task<Airport[]> task) {
						fromList.getListData().clear();
						toList.getListData().clear();

						List<String> list = new ArrayList<>();

						for(Airport a : task.getResult()) {
							list.add(a.getName());
							loaded_airports.put(a.getName(), a);
						}

						fromList.setListData(list);
						toList.setListData(list);

//						closeIndicator();
					}

					@Override
					public void executeFailed(Task<Airport[]> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);
						Alert.alert(MessageType.ERROR, e.getMessage(), SelectionController.this);

//						closeIndicator();
					}
				}));
			}
		});

		selectButton.getButtonPressListeners().add(new ButtonPressListener() {
			@Override
			public void buttonPressed(Button button) {
				final String fromAirport = (String) fromList.getSelectedItem();
				final String toAirport = (String) toList.getSelectedItem();

				if(fromAirport.equals("MissingAirports") || toAirport.equals("MissingAirports")) {
					Alert.alert(MessageType.ERROR,
						"\"MissingFlight\" is not a legal selection! Please wait for the client to load the airports!",
						SelectionController.this);
					return;
				}

				new Task<Flight[]>() {
					@Override
					public Flight[] execute() throws TaskExecutionException {
						String fromId = loaded_airports.get(fromAirport).getAirportID();
						String toId = loaded_airports.get(toAirport).getAirportID();

						Main.mailbox.send(new SearchAirportRouteMsg(System.currentTimeMillis(), fromId, toId));

						Message msg;
						while((msg = Main.mailbox.getOldestIncoming()) == null) {
							try {
								Thread.sleep(10);
							}
							catch(InterruptedException e) {
								throw new TaskExecutionException(e);
							}
						}

						if(msg instanceof SearchAirportRouteRespMsg) {
							SearchAirportRouteRespMsg resp = (SearchAirportRouteRespMsg) msg;
							return resp.getFlightList();
						}
						else {
							logger.severe("Client received incorrect message type! Received type: " + msg.getType());
							throw new TaskExecutionException("Client received incorrect message type!");
						}
					}
				}.execute(new TaskAdapter<>(new TaskListener<Flight[]>() {
					@Override
					public void taskExecuted(Task<Flight[]> task) {
						loadBookingWindow(task.getResult());
					}

					@Override
					public void executeFailed(Task<Flight[]> task) {
						Throwable e = task.getFault();
						logger.log(Level.SEVERE, e.getMessage(), e);
						Alert.alert(MessageType.ERROR, e.getMessage(), SelectionController.this);
					}
				}));
			}
		});
	}

	private void openIndicator() {
		sheet.open(Main.display, SelectionController.this);
	}

	private void closeIndicator() {
		sheet.close();
	}

	private void loadBookingWindow(Flight[] flights) {
		this.close();
		BXMLSerializer serializer = new BXMLSerializer();
		BookController window;
		try {
			window = (BookController) serializer.readObject(BookController.class, "/bxml/book.bxml");
			window.setFlights(flights);
			window.open(Main.display);
		}
		catch(IOException | SerializationException e) {
			e.printStackTrace();
		}
	}
}
