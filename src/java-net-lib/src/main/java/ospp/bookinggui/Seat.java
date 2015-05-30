package ospp.bookinggui;

public class Seat {

	public static final int ARG_AMOUNT;

	static {
		ARG_AMOUNT = Seat.class.getDeclaredFields().length - 1;
	}

	private final String  SEAT_ID;
	private final String  FLIGHT_ID;
	private final String  KLASS;
	private final boolean WINDOW;
	private final int     ROW;
	private final int     COL;
	private       boolean locked;

	public Seat(String id, String flight_id, String klass, boolean window, int row, int col,
				boolean locked) {

		this.SEAT_ID = id;
		this.FLIGHT_ID = flight_id;
		this.KLASS = klass;
		this.WINDOW = window;
		this.ROW = row;
		this.COL = col;
		this.locked = locked;
	}

	public static Seat parseBody(String[] parts, int offset) {
		if(parts.length - offset < ARG_AMOUNT) {
			throw new IllegalArgumentException("Seat.parseBodyToArray() was given a too small array!");
		}

		return new Seat(
			parts[offset],
			parts[offset + 1],
			parts[offset + 2],
			Boolean.valueOf(parts[offset + 3]),
			Integer.valueOf(parts[offset + 4]),
			Integer.valueOf(parts[offset + 5]),
			Boolean.valueOf(parts[offset + 6])
		);
	}

	public String getSeatID() {
		return this.SEAT_ID;
	}

	public String getFlightID() {
		return this.FLIGHT_ID;
	}

	public String getKlass() {
		return this.KLASS;
	}

	public boolean getWindow() {
		return this.WINDOW;
	}

	public int getRow() {
		return this.ROW;
	}

	public int getCol() {
		return this.COL;
	}

	public boolean isLocked() {
		return this.locked;
	}

	/**
	 * Set the locked value to true of this seat.
	 * <p/>
	 * NOTE! This does not send any data to the server!
	 * It only alters the boolean value of this object.
	 */
	public void setLocked() {
		this.locked = true;
	}

	/**
	 * Set the locked value to false of this seat.
	 * <p/>
	 * NOTE! This does not send any data to the server!
	 * It only alters the boolean value of this object.
	 */
	public void setUnlocked() {
		this.locked = false;
	}

	public String[] createBody() {
		return new String[]{
			SEAT_ID, FLIGHT_ID, KLASS,
			String.valueOf(WINDOW), String.valueOf(ROW), String.valueOf(COL), String.valueOf(locked)
		};
	}
}
