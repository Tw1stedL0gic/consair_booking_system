/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class SeatBookerController implements Initializable, ControlledScreen {

        @FXML
        private WebView webView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            WebEngine engine = webView.getEngine();
            engine.load("http://www.google.com");
    }    
    
    private ScreenMaster myScreenMaster;
    @Override
    public void setScreenParent(ScreenMaster sm) {
        myScreenMaster = sm;
    }
}