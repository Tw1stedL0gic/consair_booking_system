package ospp.bookinggui;

public interface Adapter {

	public void getPassengerList(Flight flight);

	public void getFlightList();

	public void login(String username, String password);

	public void disconnect();

	public void book(Flight flight, BookingInfo booking);

	public void getPassengerInfo(PAID id);
}
