/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospp.booking.bookingfx;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Andreas
 */
public class ScreenMaster extends StackPane {
    private Stage stage;
    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreenMaster(){
        super();
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
