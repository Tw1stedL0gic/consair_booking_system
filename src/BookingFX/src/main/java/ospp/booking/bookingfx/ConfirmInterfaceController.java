/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.scene.control.ListView;
import ospp.bookinggui.Flight;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class ConfirmInterfaceController implements Initializable, ControlledScreen {

    
        private HashMap<String, Flight> listNameToFlight = new HashMap<>();
    
        @FXML
        private ListView<String> listview;
        
	@FXML
	private Label flightLabel;
	@FXML
	private Label totalPass;
	//Lägg till listView'en och lägg all info från föregående sökningar för att
	//bekräfta att allt stämmer så bokningen kan bli bekräftad
	private ScreenMaster myScreenMaster;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@FXML
	void backButtonClick(ActionEvent event) {
		myScreenMaster.setScreen("additionaloptionsinterface");
	}

	@FXML
	void confirmButtonClick(ActionEvent event) {
                String s = listview.getSelectionModel().selectedItemProperty().getValue();
                
		//Skicka bekräftelseförfrågan om bokningen med alla val till databasen över nätverket
		myScreenMaster.setScreen("confirmedinterface");
	}

	@Override
	public void setScreenParent(ScreenMaster sm) {
		myScreenMaster = sm;
	}

	@Override
	public void onScreen() {
                Iterator<Flight> iter = myScreenMaster.model.flyghts.iterator();
                this.listNameToFlight = new HashMap<>(myScreenMaster.model.flyghts.size());
                listview.getItems().removeAll(listview.getItems());
                while(iter.hasNext()){
                    Flight f = iter.next();
                    String s = "from: " +f.getFrom().getName()  + "  " + f.getDeparture().toString() + "to: " + f.getTo().getName();
                    listNameToFlight.put(s, f);
                    listview.getItems().add(s);
                }
                
		flightLabel.setText(myScreenMaster.chosenFlight);
		totalPass.setText(Integer.toString(myScreenMaster.adultpass) + " adults and " + Integer.toString(myScreenMaster.childpass) + " children");
	}

	@Override
	public void offScreen() {
		flightLabel.setText("");
		totalPass.setText("");
	}
}
