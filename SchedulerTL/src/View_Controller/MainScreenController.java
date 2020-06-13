/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Util.TablePopSQL;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ten
 */
public class MainScreenController implements Initializable {
    Stage stage;
    Parent scene;
         
    @FXML
    private TableView<Appointment> weekApptTbl;
    @FXML
    private TableColumn<Appointment, LocalDate> wkDateCol;
    @FXML
    private TableColumn<Appointment, LocalTime> wkTimeCol;
    @FXML
    private TableColumn<Appointment, String> wkNameCol;
    @FXML
    private TableColumn<Appointment, String> wkLocCol;
    @FXML
    private TableView<Appointment> monApptTbl;
    @FXML
    private TableColumn<Appointment, LocalDateTime> moDateCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> moTimeCol;
    @FXML
    private TableColumn<Appointment, String> moNameCol;
    @FXML
    private TableColumn<Appointment, String> moLocCol;
    @FXML
    private TableView<Appointment> allApptTbl;
    @FXML
    private TableColumn<Appointment, LocalDate> allDateCol;
    @FXML
    private TableColumn<Appointment, LocalTime> allTimeCol;
    @FXML
    private TableColumn<Appointment, String> allNameCol;
    @FXML
    private TableColumn<Appointment, String> allLocCol;
    @FXML
    private TableColumn<Appointment, String> allUserCol;
    @FXML
    private DialogPane headerBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //greets logged in user
        headerBar.setContentText("Welcome " + LoginController.loggedIn.getUserName());
        
        //displays all appointments
        try {
            allApptTbl.refresh();
            allApptTbl.setItems(TablePopSQL.getAllAppts());

            allDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            allTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            allNameCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
            allLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            allUserCol.setCellValueFactory(new PropertyValueFactory<>("user"));
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        //displays week appointments
        try {
            weekApptTbl.refresh();
            weekApptTbl.setItems(TablePopSQL.getWeekAppts());
            
            wkDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            wkTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            wkNameCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
            wkLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        //displays month appointments
        try {
            monApptTbl.refresh();
            monApptTbl.setItems(TablePopSQL.getMonthAppts());
            
            moDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            moTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            moNameCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
            moLocCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void addApptbtn(ActionEvent event) throws IOException {
        
        allApptTbl.getItems().clear();
        weekApptTbl.getItems().clear();
        monApptTbl.getItems().clear();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddAppointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    @FXML
    void allClientsbtn(ActionEvent event) throws IOException {
        
        allApptTbl.getItems().clear();
        weekApptTbl.getItems().clear();
        monApptTbl.getItems().clear();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/ClientList.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();   
    }

    @FXML
    void allReportsbtn(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show(); 
    }

    @FXML
    void delApptbtn(ActionEvent event) throws SQLException {
        
        Appointment delFromAll = allApptTbl.getSelectionModel().getSelectedItem();
        Appointment delFromWk = weekApptTbl.getSelectionModel().getSelectedItem();
        Appointment delFromMo = monApptTbl.getSelectionModel().getSelectedItem();
        
        if(delFromAll != null) {

            Optional<ButtonType> result = deleteAlert().showAndWait();
                if(result.get() == ButtonType.YES) {
                    allApptTbl.getItems().clear();
                    weekApptTbl.getItems().clear();
                    monApptTbl.getItems().clear();
                    Appointment.delAppt(delFromAll);
            }
            else if(result.get() == ButtonType.NO) {
            event.consume();
            }
        }
        else if(delFromWk != null) {
            
            Optional<ButtonType> result = deleteAlert().showAndWait();
                if(result.get() == ButtonType.YES) {
                    allApptTbl.getItems().clear();
                    weekApptTbl.getItems().clear();
                    monApptTbl.getItems().clear();
                    Appointment.delAppt(delFromWk);
            }
            else if(result.get() == ButtonType.NO) {
            event.consume();
            }
        }
        else if(delFromMo != null) {
            
            Optional<ButtonType> result = deleteAlert().showAndWait();
                if(result.get() == ButtonType.YES) {
                    allApptTbl.getItems().clear();
                    weekApptTbl.getItems().clear();
                    monApptTbl.getItems().clear();
                    Appointment.delAppt(delFromMo);
            }
            else if(result.get() == ButtonType.NO) {
            event.consume();
            }
        }
        else {
            Alert selectAppt = new Alert(AlertType.WARNING,
                "Please select an appointment to delete.",
                ButtonType.OK);
                selectAppt.showAndWait();
        }
    }

    @FXML
    void editApptbtn(ActionEvent event) throws IOException {
        
        Appointment editFromAll = allApptTbl.getSelectionModel().getSelectedItem();
        Appointment editFromWk = weekApptTbl.getSelectionModel().getSelectedItem();
        Appointment editFromMo = monApptTbl.getSelectionModel().getSelectedItem();
        
        if(editFromAll != null && editFromAll.getUser().equals(LoginController.loggedIn.getUserName())) {
            editThisAppointment(event, editFromAll);
            allApptTbl.getItems().clear();
            weekApptTbl.getItems().clear();
            monApptTbl.getItems().clear();
        }
        else if(editFromWk != null) {
            editThisAppointment(event, editFromWk);
            allApptTbl.getItems().clear();
            weekApptTbl.getItems().clear();
            monApptTbl.getItems().clear();
        }
        else if(editFromMo != null) {
            editThisAppointment(event, editFromMo);
            allApptTbl.getItems().clear();
            weekApptTbl.getItems().clear();
            monApptTbl.getItems().clear();
        }
        else {
            Alert selectAppt = new Alert(AlertType.WARNING,
            "Please select a valid appointment to edit. You may only edit your own.",
            ButtonType.OK);
            selectAppt.showAndWait();
        }  
    }

    @FXML
    void logOutbtn(ActionEvent event) {
        
        Alert logOut = new Alert(AlertType.CONFIRMATION, 
        "Are you sure you want logout?",
        ButtonType.YES,
        ButtonType.NO);
        logOut.setTitle("Logout?");
        Optional<ButtonType> result = logOut.showAndWait();
            if(result.get() == ButtonType.YES) {
            System.exit(0);
        }
        else if(result.get() == ButtonType.NO) {
            event.consume();
        }
    }
    
    public static Alert deleteAlert() {
        
        Alert delAppt = new Alert(Alert.AlertType.CONFIRMATION, 
        "Are you sure you want to delete this appointment?",
            ButtonType.YES,
            ButtonType.NO);
            delAppt.setTitle("Delete Appointment?");
            
        return delAppt;
    }
    
    public void editThisAppointment(ActionEvent event, Appointment appt) throws IOException {
    
        Parent root;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/EditAppointment.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        EditAppointmentController controller = loader.getController();
        controller.setAppointment(appt);
    }
}
