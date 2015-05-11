package ospp.bookinggui;

public class Seat {

	private final int ROW;
	private final int COLUMN;

	public Seat(int row, int column) {
		this.ROW = row;
		this.COLUMN = column;
	}

	public int getRow() {
		return this.ROW;
	}

	public int getColumn() {
		return this.COLUMN;
	}
}
