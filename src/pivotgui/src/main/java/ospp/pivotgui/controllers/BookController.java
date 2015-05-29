package ospp.pivotgui.controllers;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.collections.List;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Window;
import ospp.bookinggui.Flight;

import javax.swing.text.html.ListView;
import java.net.URL;

public class BookController extends Window implements Bindable {

	@BXML
	private ListView flightList = null;

	private Flight[] flights = null;

	@Override
	public void initialize(Map<String, Object> map, URL url, Resources resources) {

	}

	public void setFlights(Flight[] flights) {
		this.flights = flights;
	}
}
