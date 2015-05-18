package ospp.booking.bookingfx;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {
    
    
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/SearchInterface.fxml"));
        ScreenMaster sm = new ScreenMaster();
        
        sm.loadScreen("searchinterface", "/fxml/SearchInterface.fxml");
        sm.loadScreen("bookoptioninterface", "/fxml/BookOptionInterface.fxml");
        sm.loadScreen("additionaloptionsinterface", "/fxml/AdditionalOptionsInterface.fxml");
        sm.loadScreen("confirminterface", "/fxml/ConfirmInterface.fxml");
        sm.loadScreen("confirmedinterface", "/fxml/ConfirmedInterface.fxml");
        sm.loadScreen("seatbooker", "/fxml/seatBooker.fxml");
        sm.loadScreen("scene", "/fxml/Scene.fxml");
        
        sm.setScreen("scene");
        
        Parent root = sm;
        
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/searchinterface.css");
        scene.getStylesheets().add("/styles/bookoptioninterface.css");
        scene.getStylesheets().add("/styles/additionaloptionsinterface.css");
        scene.getStylesheets().add("/styles/confirminterface.css");
        scene.getStylesheets().add("/styles/confirmedinterface.css");
        scene.getStylesheets().add("/styles/Styles.css");
        
        stage.setTitle("Cons air");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
