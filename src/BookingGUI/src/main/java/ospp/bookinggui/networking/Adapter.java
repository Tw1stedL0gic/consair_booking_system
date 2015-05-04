package ospp.bookinggui.networking;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.PAID;

public interface Adapter {

	/**
	 * Retrieves the passengerlist from the server and adds it to the inbox.
	 *
	 * @param flight The flight to get the passengerlist of.
	 */
	public void getPassengerList(Flight flight);

	/**
	 * Retrieves a list of all flights from the server and adds it to the inbox.
	 */
	public void getFlightList();

	/**
	 * Attempt to authenticate against the server with the given username and password.
	 *
	 * @param username
	 * @param password
	 */
	public void login(String username, String password);

	/**
	 * Attempt to connect to the given server.
	 *
	 * @param url
	 * @param port
	 */
	public void connect(String url, int port);

	/**
	 * Terminate the connection to the server.
	 */
	public void disconnect();

	/**
	 * Book a seat at the given flight with the given info.
	 *
	 * @param flight
	 * @param booking
	 */
	public void book(Flight flight, BookingInfo booking);

	/**
	 * Retrieves the information of the passenger with the given ID and adds it to the inbox.
	 * @param id
	 */
	public void getPassengerInfo(PAID id);
}
