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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    
    
    
    @FXML
    private Label chosenLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeline   = new Timeline();        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final String[] flyg = new String[BigData.flights.length];
        for(int i=0; i<BigData.flights.length; i++){
            flyg[i] = "From: " + BigData.flights[i][0] + " " + BigData.flights[i][2] + " " + BigData.flights[i][3] + "    To: " 
            + BigData.flights[i][4] + " " + BigData.flights[i][6] + " " + BigData.flights[i][7] + "   ";
            final String text = flyg[i];
            Label label = new Label(flyg[i]);
            label.setFont(Font.font("Arial", 20));
            Button button = new Button("Select");
            button.setOnMouseClicked(new EventHandler<MouseEvent>(){

                @Override
                public void handle(MouseEvent event) {
                    chosenLabel.setText(text);
                    myScreenMaster.chosenFlight = text;
                }
                
            });
            HBox sp = new HBox(label, button);
            label.setTextFill(Color.WHITE);
            
            vbox.getChildren().add(sp);
        }
        
        
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
        //myScreenMaster.setScreen("additionaloptionsinterface");
        myScreenMaster.setScreen("confirminterface");
    }
    
    private ScreenMaster myScreenMaster;
    @Override
    public void setScreenParent(ScreenMaster sm) {
       myScreenMaster = sm;
    }
        @Override
    public void onScreen() {

     
        //timeline.play();
    }

    @Override
    public void offScreen() {
        chosenLabel.setText("");
        timeline.stop();
    }
}
