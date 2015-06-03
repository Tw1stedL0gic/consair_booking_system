/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import ospp.bookinggui.Airport;
import ospp.bookinggui.Flight;
import ospp.bookinggui.Seat;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;
import ospp.bookinggui.networking.messages.ErrorMsg;
import ospp.bookinggui.networking.messages.InitBookMsg;
import ospp.bookinggui.networking.messages.LoginMsg;
import ospp.bookinggui.networking.messages.LoginRespMsg;
import ospp.bookinggui.networking.messages.RequestAirportsMsg;
import ospp.bookinggui.networking.messages.RequestAirportsRespMsg;
import ospp.bookinggui.networking.messages.RequestSeatSuggestionRespMsg;
import ospp.bookinggui.networking.messages.SearchAirportRouteRespMsg;

/**
 *
 * @author Erik
 */
public class Model {
    
    private Mailbox<Message> mailbox;
    private NetworkAdapter adapter;
    private Timeline  timeline;
    //private ObservableList<Message> observeMailbox;
    
    
    public ArrayList<Flight> flyghts = new ArrayList<Flight>();
    public ArrayList<Airport> airports;
    public StringProperty privilege_level = new SimpleStringProperty(null);
    public StringProperty privilege_level() { return this.privilege_level; }
    public BooleanProperty isConnected = new SimpleBooleanProperty(false);
    
    public Model() {
        
        airports = new ArrayList<Airport>();
        //demo data with out server
        for(int i=0; i<BigData.airports.length; i++){
            airports.add(new Airport(String.valueOf(i), BigData.airports[i][0], BigData.airports[i][0]));
        }
        mailbox = new Mailbox<Message>();
        //observeMailbox = FXCollections.observableList(new LinkedList<Message>());

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);

        //kollar netverket varje 10ms
        Duration duration = Duration.millis(10);
        EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                            Message oldestIncoming = mailbox.getOldestIncoming();
                            if(oldestIncoming != null) {
                                    //observeMailbox.add(oldestIncoming);
                                    onMessageReceive(oldestIncoming);
                                    timeline.playFromStart();
                            }
                    }
            };

            KeyFrame keyFrame = new KeyFrame(duration, onFinished);

            //add the keyframe to the timeline
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        
    }
    
    private void onMessageReceive(Message m){
        switch(m.getType()){
            case LOGIN_RESP:
                mailbox.send(new RequestAirportsMsg(System.currentTimeMillis(), null));
                this.privilege_level.set(((LoginRespMsg)(m)).getPrivilegeLevel().name());
                break;
            case REQ_AIRPORTS_RESP:
                Airport[] arr = ((RequestAirportsRespMsg)(m)).getAirports();
                airports = new ArrayList(arr.length);
                for(Airport a : arr){
                    airports.add(a);
                    System.out.println(a.getAirportID() + ", " + a.getName() + ", " + a.getIATA());
                }
                break;
            case ERROR:
                System.err.println( ((ErrorMsg)(m)).getErrorMessage());
                break;
            case SEARCH_ROUTE_RESP:
                flyghts = new ArrayList<Flight>();
                flyghts.addAll(Arrays.asList(((SearchAirportRouteRespMsg)(m)).getFlightList()));
            case REQ_SEAT_SUGGESTION_RESP:
                Seat[] slist = ((RequestSeatSuggestionRespMsg)(m)).getSeatList();
                boolean ok = true;
                for(Seat seat : slist){
                    if(seat.isLocked()){
                        ok = false;
                    }
                }
                if(ok){
                    for(Seat seat : slist){
                        mailbox.send(new InitBookMsg(System.currentTimeMillis(), seat.getSeatID()));
                    }
                }
                break;
            case INIT_BOOK_RESP:
                    
                    break;
            default:
                System.err.println(m.toString());
        }
    }
    
    

    
    public void connect(final String IP, final int port){
        if(adapter == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        adapter = new NetworkAdapter(mailbox, IP, port);
                        isConnected.set(true);
                    } catch (IOException ex) {
                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();
        }
    }
    
    public void login(String userName, String password){
        LoginMsg lm = new LoginMsg(System.currentTimeMillis(), userName, password);
        try {
            System.out.println(lm.createMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        mailbox.send(lm);
    }
    
    
}
