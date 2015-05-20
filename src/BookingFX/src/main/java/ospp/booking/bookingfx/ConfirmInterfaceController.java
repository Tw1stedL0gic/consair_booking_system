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
public class ConfirmInterfaceController implements Initializable, ControlledScreen {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    //Lägg till listView'en och lägg all info från föregående sökningar för att
    //bekräfta att allt stämmer så bokningen kan bli bekräftad
    
    
    @FXML
    void backButtonClick(ActionEvent event) {
        myScreenMaster.setScreen("additionaloptionsinterface");
    }
    @FXML
    void confirmButtonClick(ActionEvent event) {
        //Bekräfta bokningen med alla val till databasen över nätverket
        myScreenMaster.setScreen("confirmedinterface");
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
