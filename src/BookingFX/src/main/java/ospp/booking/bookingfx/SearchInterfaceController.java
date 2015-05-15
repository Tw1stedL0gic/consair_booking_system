package ospp.booking.bookingfx;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.util.converter.ShortStringConverter;






/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class SearchInterfaceController implements Initializable, ControlledScreen {

    private int adultNr = 0;
    private int childNr = 0;
    private int totalNr = 0;
    
    @FXML
    private TitledPane totalLabel;

    @FXML
    private Label adultLabel;

    @FXML
    private Label childLabel;
   

    @FXML
    private DatePicker returnCalendar;


    @FXML
    private CheckBox turReturBox;

    @FXML
    private ComboBox fromComboBox;
    
    @FXML
    private ComboBox toComboBox;
    
    @FXML
    private TextField fromField;
    
    @FXML
    private TextField toField;
    
    @FXML
    private ListView fromListView;
    
    @FXML
    private ListView toListView;
    
    @FXML
    void adultDecClick(ActionEvent event) {
        if (adultNr > 0){
            adultNr--;
        }
        updateTotalNr();
    }

    @FXML
    void adultIncClick(ActionEvent event) {
        adultNr++;
        updateTotalNr();
    }

    @FXML
    void childDecClick(ActionEvent event) {
        if(childNr > 0){
            childNr--;
        }
        updateTotalNr();
    }

    @FXML
    void childIncClick(ActionEvent event) {
        childNr++;
        updateTotalNr();
    }
    
    @FXML
    void searchButtonClick(ActionEvent event) {
        myScreenMaster.setScreen("bookoptioninterface");
    }
    
    
    void updateTotalNr(){
        
        if(adultNr == 1){
            adultLabel.textProperty().set(adultNr + " Adult");
        }
        else{
            adultLabel.textProperty().set(adultNr + " Adults");
        }
        if(childNr == 1){
            childLabel.textProperty().set(childNr + " Child");
        }
        else{
            childLabel.textProperty().set(childNr + " Children");
        }
        totalNr = adultNr + childNr;
        if(totalNr == 1){
            totalLabel.textProperty().set(totalNr + " Passenger");
        }
        else{
            totalLabel.textProperty().set(totalNr + " Passengers");
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateTotalNr();
        totalLabel.expandedProperty().set(false);
        returnCalendar.disableProperty().bind(turReturBox.selectedProperty().not());
        //final String[] flygplatser = {"arlanda", "arlunda", "inte arlanda", "inte uppsala", "nÃ¥got annat"};
        final String[] flygplatser = new String[BigData.airports.length];
        for(int i=0; i<BigData.airports.length; i++){
            flygplatser[i] = BigData.airports[i][0] + " " + BigData.airports[i][1];
        
        }
       
        fromListView.visibleProperty().set(false);
        toListView.visibleProperty().set(false);
        
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                String string = (String) toField.getText();
                if(string == null)
                    string = "";
                System.out.println(string);
                 fromComboBox.show();
                fromComboBox.getItems().removeAll(flygplatser);
                fromComboBox.getItems().addAll(flygplatser);
                for(int i=0; i<string.length();i++){
                    for(String flygplats : flygplatser){
                        if(fromComboBox.getItems().contains(flygplats) && flygplats.charAt(i) != string.charAt(i)){
                            fromComboBox.getItems().remove(flygplats);
                        }
                    }
                }
                
               
                if(string.length() > 0){
                    for (int i = 0; i<string.length(); i++){
                        String input = "";
                        input = input + (string.charAt(i));
                        if(fromComboBox.getItems().contains(input) && input.charAt(i) != string.charAt(i)){
                            fromComboBox.getItems().remove(input);
                        }
                        fromComboBox.show();
                        
                    }
                }
            }
            
        };
        fromField.textProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("Oldvalue " + oldValue + ". Newvalue " + newValue);
                fromListView.getItems().removeAll(flygplatser);
                fromListView.getItems().addAll(flygplatser);
                
                if (newValue.length() <1){
                    fromListView.visibleProperty().set(false);
                }
                else {
                    fromListView.visibleProperty().set(true);
                    String string = newValue;
                    for(int i=0; i<string.length();i++){
                    for(String flygplats : flygplatser){
                        if(fromListView.getItems().contains(flygplats) && flygplats.toLowerCase().charAt(i) != string.toLowerCase().charAt(i)){
                            fromListView.getItems().remove(flygplats);
                        }
                    }
                }
                    
                }
                
                
            }   
            
        });
       
        fromListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("Old click value " 
                + oldValue + " New Click value = " + newValue);
        fromField.textProperty().set(newValue);
        fromListView.getItems().remove(newValue);
        }
        });
        
        
        toField.textProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("Oldvalue " + oldValue + ". Newvalue " + newValue);
                toListView.getItems().removeAll(flygplatser);
                toListView.getItems().addAll(flygplatser);
                
                if (newValue.length() <1){
                    toListView.visibleProperty().set(false);
                }
                else {
                    toListView.visibleProperty().set(true);
                    String string = newValue;
                    for(int i=0; i<string.length();i++){
                    for(String flygplats : flygplatser){
                        if(toListView.getItems().contains(flygplats) && flygplats.toLowerCase().charAt(i) != string.toLowerCase().charAt(i)){
                            toListView.getItems().remove(flygplats);
                        }
                    }
                }
                    
                }
                
                
            }   
            
        });
       
        toListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        System.out.println("Old click value " 
                + oldValue + " New Click value = " + newValue);
        toField.textProperty().set(newValue);
        toListView.getItems().remove(newValue);
        }
        });
        
    }    

    
    
    
    
    private ScreenMaster myScreenMaster;
    @Override
    public void setScreenParent(ScreenMaster sm) {
        myScreenMaster = sm;
    }
    
}
