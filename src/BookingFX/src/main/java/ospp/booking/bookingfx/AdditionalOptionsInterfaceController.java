/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class AdditionalOptionsInterfaceController implements Initializable, ControlledScreen {

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
    
    
    private ScreenMaster myScreenMaster;
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
