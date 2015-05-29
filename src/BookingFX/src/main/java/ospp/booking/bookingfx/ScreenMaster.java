/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ospp.bookinggui.networking.Mailbox;
import ospp.bookinggui.networking.Message;
import ospp.bookinggui.networking.NetworkAdapter;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Andreas
 */
public class ScreenMaster extends StackPane {

        public Model model;
	public String chosenFlight = "";
	public int    adultpass    = 0;
	public int    childpass    = 0;
	private Stage            stage;
	
	
	private String                            activeScreen = null;
	private HashMap<String, Parent>           screens      = new HashMap();
	private HashMap<String, ControlledScreen> controllers  = new HashMap();
	public ScreenMaster() {
		super();
		this.backgroundProperty().set(Background.EMPTY);
                this.model = new Model();
		
        /*
        try {
            networkAdapter = new NetworkAdapter(mailbox, url, port);
        } catch (IOException ex) {
            System.err.println("Fel!!!!!");
            Logger.getLogger(ScreenMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
                */
	}






	public void addScreen(String name, Parent screen, ControlledScreen cont) {
		screens.put(name, screen);
		controllers.put(name, cont);
	}

	public Parent getScreen(String name) {
		return screens.get(name);
	}

	public boolean loadScreen(String name, String resource) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
			Parent loadScreen = myLoader.load();
			ControlledScreen myScreenControler = (myLoader.getController());
			myScreenControler.setScreenParent(this);
			addScreen(name, loadScreen, myScreenControler);
			return true;
		}
		catch(Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	public boolean setScreen(final String name) {
		String oldActiveScreen = activeScreen;
		activeScreen = name;
		if(screens.get(name) != null) {
			if(!getChildren().isEmpty()) {
				controllers.get(oldActiveScreen).offScreen();
				getChildren().remove(0);
				getChildren().add(0, screens.get(name));

			}
			else {
				getChildren().add(screens.get(name));
			}
			controllers.get(name).onScreen();
			return true;
		}
		else {
			System.err.println("screen has not been loaded \n");
			return false;
		}
	}


}
