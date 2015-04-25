package ospp.booking.bookingfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private TextArea textarea;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        textarea.setText("Hello World!");
        textarea.textProperty().set("FooBar");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
