/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.Platform.exit;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class ConfirmedInterfaceController implements Initializable, ControlledScreen {

	private ScreenMaster myScreenMaster;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@FXML
	void startpageButtonClick(ActionEvent event) {
		myScreenMaster.setScreen("searchinterface");
	}

	@FXML
	void quitButtonClick(ActionEvent event) {
		exit();
		//myScreenMaster.setScreen("bookoptioninterface");
	}

	@Override
	public void setScreenParent(ScreenMaster sm) {
		myScreenMaster = sm;
	}

	@Override
	public void onScreen() {
	}

	@Override
	public void offScreen() {
	}
}
