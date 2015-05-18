package ospp.bookinggui;

public class Passenger {
	private final long   IDENTIFICATION;
	private final String NAME;
	private final String ADDRESS;
	private final String PAYMENT_INFO;
	private final String EMAIL;

	public Passenger(long id, String name, String address, String payment_info, String email) {
		this.IDENTIFICATION = id;
		this.NAME = name;
		this.ADDRESS = address;
		this.PAYMENT_INFO = payment_info;
		this.EMAIL = email;
	}

	public long getIdentification() {
		return IDENTIFICATION;
	}

	public String getName() {
		return this.NAME;
	}

	public String getAddress() {
		return this.ADDRESS;
	}

	public String getPaymentInfo() {
		return this.PAYMENT_INFO;
	}

	public String getEmail() {
		return this.EMAIL;
	}
}