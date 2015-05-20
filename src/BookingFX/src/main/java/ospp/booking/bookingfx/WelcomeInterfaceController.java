/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class WelcomeInterfaceController implements Initializable, ControlledScreen {
    private Timeline timeline;
    
    @FXML
    private ImageView consAir;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //create a timeline for moving the circle
        timeline = new Timeline();        
        timeline.setCycleCount(1);
        timeline.setAutoReverse(true);
 
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                myScreenMaster.setScreen("searchinterface");
            }
        };
        //one can start/pause/stop/play animation by
        //timeline.play();
        //timeline.pause();
        //timeline.stop();
        //timeline.playFromStart();
         
        //add the following keyframes to the timeline
        timeline.getKeyFrames().addAll
            (new KeyFrame(Duration.ZERO,
                          new KeyValue(consAir.scaleXProperty(), 0.0),
                          new KeyValue(consAir.rotateProperty(), -1*1440),
                          new KeyValue(consAir.scaleYProperty(), 0.0)),
         
             new KeyFrame(new Duration(2000), onFinished,
                          new KeyValue(consAir.scaleXProperty(), 1),
                          new KeyValue(consAir.rotateProperty(), 0),  
                          new KeyValue(consAir.scaleYProperty(), 1)));
        
    }
    
    private ScreenMaster myScreenMaster;
    @Override
    public void setScreenParent(ScreenMaster sm) {
        this.myScreenMaster = sm;
    }

    @Override
    public void onScreen() {
        timeline.play();
    }

    @Override
    public void offScreen() {
    }
    
}
