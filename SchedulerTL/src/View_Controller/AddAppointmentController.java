/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Customer;
import Util.DateTime;
import Util.TablePopSQL;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ten
 */
public class AddAppointmentController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    LocalDate date;
    String selStart;
    String selEnd;

    @FXML
    private TextField titletxt;
    @FXML
    private TextField clientTxt;
    @FXML
    private TextField locationtxt;
    @FXML
    private TextField contacttxt;
    @FXML
    private TextField typetxt;
    @FXML
    private TextField urltxt;
    @FXML
    private DatePicker dateTxt;
    @FXML
    private ComboBox<String> startTimedrp;
    @FXML
    private ComboBox<String> endTimedrp;
    @FXML
    private TextArea descriptiontxt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        descriptiontxt.setFocusTraversable(true);
        startTimedrp.setItems(Appointment.allStartTimes);
        endTimedrp.setItems(Appointment.allEndTimes);
        dateTxt.setValue(LocalDate.now());
        startTimedrp.setValue("00:00");
        endTimedrp.setValue("00:00");
    }    

    @FXML
    void addApptbtn(ActionEvent event) throws IOException, SQLException {
        
        searchCustomer(event);
        boolean validAppt = Appointment.isNull(clientTxt.getText(), titletxt.getText(), descriptiontxt.getText(), locationtxt.getText(), 
                            contacttxt.getText(), typetxt.getText(), urltxt.getText(), dateTxt.getValue(),
                            startTimedrp.getValue(), endTimedrp.getValue());
        
            if(validAppt != false){
                Alert nullFields = new Alert(Alert.AlertType.WARNING,
                "Please enter valid date for all fields.",
                ButtonType.OK);
                nullFields.showAndWait();     
            }
            else {
                String startDT = dateTxt.getValue() + " " + startTimedrp.getValue();
                String endDT = dateTxt.getValue() + " " + endTimedrp.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime start = LocalDateTime.parse(startDT, formatter);
                LocalDateTime end = LocalDateTime.parse(endDT, formatter);
                
                if(DateTime.timeOverlap(start, end, 0) != false) {
                    Alert startOverlap = new Alert(Alert.AlertType.WARNING,
                    "Appointment time overlaps with another appointment. Try again.",
                    ButtonType.OK);
                    startOverlap.showAndWait();   
                }
                else if(start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
                    Alert apptPast = new Alert(Alert.AlertType.WARNING,
                    "Appointment cannot be in the past. Please try again.",
                    ButtonType.OK);
                    apptPast.showAndWait();
                }
                else if(start.toLocalTime().isBefore(LocalTime.of(8, 00)) || end.toLocalTime().isAfter(LocalTime.of(18, 00)) ||
                    start.toLocalTime().isAfter(end.toLocalTime()) || end.toLocalTime().isBefore(start.toLocalTime())) {
                    Alert outsideHours = new Alert(Alert.AlertType.WARNING,
                    "Appointment cannot before or after business hours. Please try again.",
                    ButtonType.OK);
                    outsideHours.showAndWait();
                }
                else{
                    try {
                    Appointment.addAppointment(clientTxt.getText(), titletxt.getText(), descriptiontxt.getText(), locationtxt.getText(), 
                            contacttxt.getText(), typetxt.getText(), urltxt.getText(), start, end);
                    
                    TablePopSQL.setAllAppts();
                    TablePopSQL.setWeekAppts();
                    TablePopSQL.setMonthAppts();
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                    } 
                    catch (Exception e) {
                        e.printStackTrace();
                }
            }
        }       
    }

    @FXML
    void cancelAddbtn(ActionEvent event) throws IOException, SQLException {
        
        Alert Cancel = new Alert(Alert.AlertType.CONFIRMATION, 
        "Cancel and return to the previous page? No changes will be saved.",
        ButtonType.YES,
        ButtonType.NO);
        Cancel.setTitle("Cancel?");
        Optional<ButtonType> result = Cancel.showAndWait();
            if(result.get() == ButtonType.YES) {
            TablePopSQL.setAllAppts();
            TablePopSQL.setWeekAppts();
            TablePopSQL.setMonthAppts();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else if(result.get() == ButtonType.NO) {
            event.consume();
        }
    }

    @FXML
    void dateSel(ActionEvent event) {
        
        if (dateTxt.getValue().isBefore(LocalDate.now())) {
            Alert outOfRange = new Alert(Alert.AlertType.WARNING,
            "Date cannot be in the past. Try again.",
            ButtonType.OK);
            outOfRange.showAndWait();
            }
        else {
            this.date = dateTxt.getValue();
        }
    }

    @FXML
    void searchCustomer(ActionEvent event) throws SQLException {
        
        if (clientTxt.getText().isEmpty() || clientTxt.getText() == null) {
            Alert customerSearch = new Alert(Alert.AlertType.WARNING,
            "Please select an existing customer.",
            ButtonType.OK);
            customerSearch.showAndWait();
        }
        else {
        String[] getClient = Customer.searchCustomer(clientTxt.getText());
            if (getClient != null) {
                clientTxt.setText(getClient[0]);
                contacttxt.setText(getClient[1]);
            }
            else {
                Alert customerSearch = new Alert(Alert.AlertType.WARNING,
                "No existing client found. Please add new client or update existing one and try again.",
                ButtonType.OK);
                customerSearch.showAndWait();
            }
        }
    }
    
    @FXML
    void startSel(ActionEvent event) {
        
        this.selStart = startTimedrp.getSelectionModel().getSelectedItem();
        try {
            LocalTime.parse(selStart);
        }
        catch(DateTimeException e) {
            Alert invalidTime = new Alert(Alert.AlertType.WARNING,
            "Time is not a valid format. Please try again",
            ButtonType.OK);
            invalidTime.showAndWait();     
        }          
    }

    @FXML
    void endSel(ActionEvent event) {
        
        this.selEnd = startTimedrp.getSelectionModel().getSelectedItem();
        try {
            LocalTime.parse(selEnd);
        }
        catch(DateTimeException e) {
            Alert invalidTime = new Alert(Alert.AlertType.WARNING,
            "Time is not a valid format. Please try again",
            ButtonType.OK);
            invalidTime.showAndWait();
        }
    }    
}
