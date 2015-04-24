package ospp.bookinggui;

public interface NetworkAdapter {

	public void getPassengerList(String flightnumber);

	public void getFlightList();

	public void login(String username, String password);

	public void disconnect();

	public void book(String flightnumber, BookingInfo booking);

	public void getPassengerInfo(PAID id);
}
