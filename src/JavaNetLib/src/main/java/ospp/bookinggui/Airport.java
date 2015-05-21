package ospp.bookinggui;

public class Airport {
	private final String ID;
	private final String IATA;
	private final String NAME;

	public Airport(String airport_id, String iata, String name) {
		this.ID = airport_id;
		this.IATA = iata;
		this.NAME = name;
	}

	public String getAirportID() {
		return this.ID;
	}

	public String getIATA() {
		return this.IATA;
	}

	public String getName() {
		return this.NAME;
	}
}
