package ospp.bookinggui.networking;

import ospp.bookinggui.BookingInfo;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Passenger;

public interface Adapter {

	/**
	 * Retrieves the passengerlist from the server and adds it to the inbox.
	 *
	 * @param flight The flight to get the passengerlist of.
	 */
	void getPassengerList(Flight flight);

	/**
	 * Retrieves a list of all flights from the server and adds it to the inbox.
	 */
	void getFlightList();

	/**
	 * Attempt to authenticate against the server with the given username and password.
	 *
	 * @param username
	 * @param password
	 */
	void login(String username, String password);

	/**
	 * Terminate the connection to the server.
	 */
	void disconnect();

	/**
	 * Book a seat with the given information.
	 *
	 * @param booking
	 */
	void book(BookingInfo booking);

	/**
	 * Retrieves the information of the passenger with the given ID and adds it to the inbox.
	 *
	 * @param id
	 */
	void getPassengerInfo(Passenger id);
}