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
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class BookOptionInterfaceController implements Initializable, ControlledScreen {

    @FXML
    private ListView<?> planeList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    @FXML
    void backButtonClick(ActionEvent event) {
        myScreenMaster.setScreen("searchinterface");
    }
    
    @FXML
    void nextButtonClick(ActionEvent event) {
        myScreenMaster.setScreen("additionaloptionsinterface");
    }
    
    private ScreenMaster myScreenMaster;
    @Override
    public void setScreenParent(ScreenMaster sm) {
       myScreenMaster = sm;
    }
    
}
