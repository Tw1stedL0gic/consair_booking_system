package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.HashMap;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.TableView;
import org.apache.pivot.wtk.Window;
import ospp.bookinggui.Flight;

import java.net.URL;

public class BookController extends Window implements Bindable {

	@BXML
	private TableView  flightList   = null;
	@BXML
	private PushButton selectButton = null;

	private Flight[] flights = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

		// Load flights into flightList
		ArrayList<HashMap> tableRows = new ArrayList<>();

		for(Flight f : flights) {
			HashMap<String, String> row = new HashMap<>();

			row.put("flightID", f.getFlightID());
			row.put("flightNumber", f.getFlightNumber());
			row.put("fromAirport", f.getFrom().getName());
			row.put("toAirport", f.getTo().getName());
			row.put("departure", f.getDeparture().toString());
			row.put("arrival", f.getArrival().toString());
		}

		flightList.setTableData(tableRows);


	}

	public void setFlights(Flight[] flights) {
		this.flights = flights;
	}
}
