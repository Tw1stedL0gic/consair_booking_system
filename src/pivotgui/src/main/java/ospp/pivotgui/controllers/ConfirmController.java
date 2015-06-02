package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.Window;
import ospp.bookinggui.Seat;

import java.net.URL;

public class ConfirmController extends Window implements Bindable {

	@BXML
	private TableView seatTable = null;

	private Seat[] seat_list = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

		// Load seats into the table view
		if(seat_list != null) {
			ArrayList<String> table = new ArrayList<>();

			for(Seat s : seat_list) {
				HashMap<String, String> row = new HashMap<>();

				row.put("seatID", s.getSeatID());
				row.put("class", s.getKlass());
				row.put("window", String.valueOf(s.getWindow()));
				row.put("row", String.valueOf(s.getRow()));
				row.put("column", String.valueOf(s.getCol()));
				row.put("locked", String.valueOf(s.isLocked()));
			}
		}
	}

	public void setSeats(Seat[] seats) {
		this.seat_list = seats;
	}
}
