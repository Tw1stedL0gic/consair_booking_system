package ospp.booking.bookingfx;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;





/**
 * FXML Controller class
 *
 * @author Andreas
 */
public class SearchInterfaceController implements Initializable {

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
    }    
    
}
