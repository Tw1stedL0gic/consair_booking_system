package ospp.bookinggui;

public class Passenger {
	private final long   IDENTIFICATION;
	private final String FIRST_NAME;
	private final String SECOND_NAME;
	private final String COUNTRY;
	private final String ZIP_CODE;
	private final String STREET_ADRESS;
	private final int    HOUSE_NUMBER;

	public Passenger(long id, String first_name, String second_name, String country, String zip_code, String street_adress, int house_number) {
		this.IDENTIFICATION = id;
		this.FIRST_NAME = first_name;
		this.SECOND_NAME = second_name;
		this.COUNTRY = country;
		this.ZIP_CODE = zip_code;
		this.STREET_ADRESS = street_adress;
		this.HOUSE_NUMBER = house_number;
	}

	public long getIdentification() {
		return IDENTIFICATION;
	}

	public String getFirstName() {
		return FIRST_NAME;
	}

	public String getSecondName() {
		return SECOND_NAME;
	}

	public String getCountry() {
		return COUNTRY;
	}

	public String getZipCode() {
		return this.ZIP_CODE;
	}

	public String getStreetAdress() {
		return STREET_ADRESS;
	}

	public int getHouseNumber() {
		return HOUSE_NUMBER;
	}
}