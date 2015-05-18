/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Adapter;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.messages.HandshakeRespMsg;

/**
 *
 * @author Andreas
 */
public class ScreenMaster extends StackPane {
    private Stage stage;
    private Mailbox<Message> mailbox;
    private Adapter networkAdapter = null;
    private Timeline timeline;
    private ObservableList<Message> observeMailbox;
    
    private HashMap<String, Node> screens = new HashMap<>();
    
    public Adapter getAdapter() { return this.networkAdapter; }
    
    public void setAdapter(Adapter a) { 
        this.networkAdapter = a; 
        timeline.play();
    };
    
    public Mailbox<Message> getMailbox(){return this.mailbox;};
    
    public ScreenMaster() {
        super();
        mailbox = new Mailbox<>();
        observeMailbox = FXCollections.observableList(new LinkedList<Message>());
        
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        
        //create a keyFrame, the keyValue is reached at time 2s
        Duration duration = Duration.millis(10);
        //one can add a specific action when the keyframe is reached
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Message oldestIncoming = mailbox.getOldestIncoming();
                if(oldestIncoming != null){
                    for(int i=0; i<10 && oldestIncoming != null; i++){
                        observeMailbox.add(oldestIncoming);
                        oldestIncoming = mailbox.getOldestIncoming();
                    }
                }
            }
            
            
        };
        
        KeyFrame keyFrame = new KeyFrame(duration, onFinished);
 
        //add the keyframe to the timeline
        timeline.getKeyFrames().add(keyFrame);
        
        /*
        try {
            networkAdapter = new NetworkAdapter(mailbox, url, port);
        } catch (IOException ex) {
            System.err.println("Fel!!!!!");
            Logger.getLogger(ScreenMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
                */
    }
    
    public void addScreen(String name, Node screen){
        screens.put(name, screen);
    }
    
    public Node getScreen(String name){
        return screens.get(name);
    }
    
    public boolean loadScreen(String name, String resource){
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
    public boolean setScreen(final String name){
        if (screens.get(name) != null){
            if(!getChildren().isEmpty()){
                getChildren().remove(0);
                getChildren().add(0, screens.get(name));
            } else {
                getChildren().add(screens.get(name));
            }
        
            return true;
        } else { 
            System.err.println("screen has not been loaded \n");
            return false;
        }
    }
}
