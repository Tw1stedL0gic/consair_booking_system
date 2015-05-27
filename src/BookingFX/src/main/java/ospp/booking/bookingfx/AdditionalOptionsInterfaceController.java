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

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class AdditionalOptionsInterfaceController implements Initializable, ControlledScreen {

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
		myScreenMaster.setScreen("bookoptioninterface");
	}

	@FXML
	void nextButtonClick(ActionEvent event) {
		//Skicka vidare planet och alternativa specialval via nätverk, gå vidare till confirmInterface
		myScreenMaster.setScreen("confirminterface");
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
