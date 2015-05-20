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
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class BookOptionInterfaceController implements Initializable, ControlledScreen {

  private Timeline timeline;
    
    @FXML 
    private VBox vbox;
    // Visa resultat från sökningen på föregående sida
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline   = new Timeline();        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        
        
        
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Rectangle rect = new Rectangle(400, 100, Color.color(Math.random(), Math.random(), Math.random()));
                Timeline rtimeline   = new Timeline();        
                rtimeline.setCycleCount(Timeline.INDEFINITE);
                rtimeline.setAutoReverse(true);
                rtimeline.getKeyFrames().addAll(
                    (new KeyFrame(Duration.ZERO, new KeyValue(rect.heightProperty(), 0.0))),
                    (new KeyFrame(new Duration(3000), new KeyValue(rect.heightProperty(), 100.0))));
                rtimeline.play();
                    vbox.getChildren().add(rect);
                
            }
        };
        
         timeline.getKeyFrames().add(
             new KeyFrame(new Duration(1000),onFinished));
    }    
    
    
    @FXML
    void backButtonClick(ActionEvent event) {
        myScreenMaster.setScreen("searchinterface");
    }
    
    @FXML
    void nextButtonClick(ActionEvent event) {
        //Valt plan i listan är planet vi vill boka och ska skickas till additionalOptionsInterface
        myScreenMaster.setScreen("additionaloptionsinterface");
    }
    
    private ScreenMaster myScreenMaster;
    @Override
    public void setScreenParent(ScreenMaster sm) {
       myScreenMaster = sm;
    }
        @Override
    public void onScreen() {

     
        timeline.play();
    }

    @Override
    public void offScreen() {
        timeline.stop();
    }
}
