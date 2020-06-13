/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Country;
import Model.User;
import Util.TablePopSQL;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ten
 */
public class LoginController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    ResourceBundle res = ResourceBundle.getBundle("LanguageRes/res", Locale.getDefault());
    
    public static User loggedIn = new User();
    
    String filename = "loggedusers.txt";
    ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());

    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Label usernameLbl;
    @FXML
    private Label passwordLbl;
    @FXML
    private Button loginBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Label errorMsgTxt;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            User.setUsers();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        this.usernameLbl.setText(res.getString("usernameLbl") + ":");
        this.passwordLbl.setText(res.getString("passwordLbl") + ":");
        this.loginBtn.setText(res.getString("loginBtn"));
        this.exitBtn.setText(res.getString("exitBtn"));
    }
    
    @FXML
    void exitAction(ActionEvent event) {
        
        System.exit(0);    
    }

    @FXML
    void loginAction(ActionEvent event) throws SQLException, IOException {
        
        //checks user list to check for username and password comparison
        String user = usernameTxt.getText();
        String password = passwordTxt.getText();
        int loginId = User.loginUser(user, password);
        if (loginId == 0) {
            
            this.errorMsgTxt.setTextFill(Paint.valueOf("RED"));
            this.errorMsgTxt.setText(res.getString("errorMsgTxt"));   
        }
        else {
            
            loggedIn = new User(user, loginId);
            
            String logInfo = loggedIn.getUserName() + " logged in at " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(zdt);
            makeUserLog(logInfo);
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            
            //sets all observable lists for the appropriate user
            TablePopSQL.setAllAppts();
            TablePopSQL.setWeekAppts();
            TablePopSQL.setMonthAppts();
            TablePopSQL.setAllCustomers(); 
            Country.setAllCountries();
            
            //lambda to quickly check the user's daily appointments for any listed within the next 15 minutes
            ObservableList<Appointment> fifteenMin = TablePopSQL.getWeekAppts();
            fifteenMin.forEach((a) -> {
                int i = a.getStartTime().compareTo(LocalTime.now().plusMinutes(15));
                int j = a.getStartTime().compareTo(LocalTime.now());
                if(a.getStartDate().equals(LocalDate.now()) && i < 0 && j > 0) {
                    Alert upcommingAppt = new Alert(Alert.AlertType.INFORMATION,
                    "You have an upcomming appointment!",
                    ButtonType.OK);
                    upcommingAppt.showAndWait();
                }
            });
        }
    }
    
    //file log for user login times and dates
    public void makeUserLog(String userInfo) throws IOException {
        
        FileWriter userLog = new FileWriter(filename, true);
        PrintWriter printLog = new PrintWriter(userLog);
        printLog.printf( "%s" + "%n" , userInfo);
        printLog.close();
    }
}
