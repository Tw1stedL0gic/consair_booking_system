package ospp.booking.bookingfx;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import ospp.bookinggui.Airport;


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
	private TextField fromField;

	@FXML
	private TextField toField;

	@FXML
	private ListView fromListView;

	@FXML
	private ListView toListView;

	@FXML
	private Label errorLabel;

	@FXML
	private DatePicker toCalendar;
	private Timeline   errorline;
	private ScreenMaster myScreenMaster;

        private ArrayList<Airport> airports = new ArrayList<>();
        
	@FXML
	void adultDecClick(ActionEvent event) {
		if(adultNr > 0) {
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
		if(childNr > 0) {
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
		if(!fromField.getText().equals("") && !toField.getText().equals("") && toCalendar.getValue() != null && totalNr > 0) {
			//Sökning i databasen(nätverk) och visa resultat i bookOptionInterface
			myScreenMaster.setScreen("bookoptioninterface");
		}
		else {

			errorLabel.setVisible(true);
			errorline.playFromStart();
		}
	}

	void updateTotalNr() {
		if(adultNr == 1) {
			adultLabel.textProperty().set(adultNr + " Adult");
		}
		else {
			adultLabel.textProperty().set(adultNr + " Adults");
		}
		if(childNr == 1) {
			childLabel.textProperty().set(childNr + " Child");
		}
		else {
			childLabel.textProperty().set(childNr + " Children");
		}
		totalNr = adultNr + childNr;
		if(totalNr == 1) {
			totalLabel.textProperty().set(totalNr + " Passenger");
		}
		else {
			totalLabel.textProperty().set(totalNr + " Passengers");
		}
		myScreenMaster.childpass = childNr;
		myScreenMaster.adultpass = adultNr;
	}

        
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		totalLabel.expandedProperty().set(false);
		errorLabel.setVisible(false);
		returnCalendar.disableProperty().bind(turReturBox.selectedProperty().not());
		//final String[] flygplatser = {"arlanda", "arlunda", "inte arlanda", "inte uppsala", "nÃ¥got annat"};
		final String[] flygplatser = new String[BigData.airports.length];
		for(int i = 0; i < BigData.airports.length; i++) {
			flygplatser[i] = BigData.airports[i][0] + " " + BigData.airports[i][1];

		}

		fromListView.visibleProperty().set(false);
		toListView.visibleProperty().set(false);

		fromField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("Oldvalue " + oldValue + ". Newvalue " + newValue);
                                for(Airport a : airports){
                                    fromListView.getItems().remove(a.getName());
                                }
                                for(Airport a : airports){
                                    fromListView.getItems().add(a.getName());
                                }
				
				

				if(newValue.length() < 1) {
					fromListView.visibleProperty().set(false);
				}
				else {
					fromListView.visibleProperty().set(true);
					String string = newValue;
					for(int i = 0; i < string.length(); i++) {
						for(Airport a : airports) {
                                                        String flygplats = a.getName();
							if(fromListView.getItems().contains(flygplats) && flygplats.toLowerCase().charAt(i) != string.toLowerCase().charAt(i)) {
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
				fromListView.setVisible(false);
			}
		});


		toField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("Oldvalue " + oldValue + ". Newvalue " + newValue);
				for(Airport a : airports){
                                    fromListView.getItems().remove(a.getName());
                                }
                                for(Airport a : airports){
                                    fromListView.getItems().add(a.getName());
                                }

				if(newValue.length() < 1) {
					toListView.visibleProperty().set(false);
				}
				else {
					toListView.visibleProperty().set(true);
					String string = newValue;
					for(int i = 0; i < string.length(); i++) {
						for(Airport a : airports) {
                                                        String flygplats = a.getName();
							if(toListView.getItems().contains(flygplats) && flygplats.toLowerCase().charAt(i) != string.toLowerCase().charAt(i)) {
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
				toListView.setVisible(false);

			}
		});


		errorline = new Timeline();
		errorline.setCycleCount(1);
		errorline.autoReverseProperty().set(false);

		errorline.getKeyFrames().addAll(
			new KeyFrame(Duration.ZERO,
				new KeyValue(errorLabel.opacityProperty(), 1)),

			new KeyFrame(new Duration(5000),
				new KeyValue(errorLabel.opacityProperty(), 1)),

			new KeyFrame(new Duration(7000),
				new KeyValue(errorLabel.opacityProperty(), 0)));

	}

	@Override
	public void setScreenParent(ScreenMaster sm) {
		myScreenMaster = sm;
	}

	@Override
	public void onScreen() {
                this.airports = myScreenMaster.model.airports;
		fromField.setText("");
		toField.setText("");
		toCalendar.setValue(null);
		childNr = 0;
		adultNr = 0;
		updateTotalNr();
	}

	@Override
	public void offScreen() {
	}
}
