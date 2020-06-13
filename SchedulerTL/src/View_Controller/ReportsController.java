/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Appointment;
import Model.Country;
import Model.Customer;
import Model.User;
import Util.DBConnect;
import Util.DBQuery;
import Util.TablePopSQL;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ten
 */
public class ReportsController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TextArea reportBox;
    @FXML
    private RadioButton tpmTog;
    @FXML
    private RadioButton schedTog;
    @FXML
    private RadioButton cbcTog;
    @FXML
    private ToggleGroup reportGrp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tpmTog.setToggleGroup(reportGrp);
        schedTog.setToggleGroup(reportGrp);
        cbcTog.setToggleGroup(reportGrp);
    }    

    @FXML
    void backBtn(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void tpmSelect(ActionEvent event) throws SQLException {
        
        //DB statement to generate a count of each type of appointment
        if(tpmTog.isSelected()) {
            reportBox.clear();
            Connection conn = DBConnect.conn;
            String types = "SELECT MONTHNAME(start) AS \"month\", type, COUNT(*) AS \"count\" FROM appointment\n" +
            "GROUP BY type\n" +
            "ORDER BY \"month\";";
            
            DBQuery.setPreparedStatement(conn, types);
            PreparedStatement ps = DBQuery.getPreparedStatement();
            ps.execute();
            
            ResultSet rs = ps.getResultSet();
            while(rs.next()) {
            reportBox.appendText(rs.getString("month") + "\n" + 
                    "   Appointment Type: " + rs.getString("type") + "= " + rs.getInt("count") + "\n");
            }
        }     
    }

    @FXML
    private void schedSelect(ActionEvent event) {
        
        //lambdas to efficiently attach appointment dates and times under each user for current month schedule
        if(schedTog.isSelected()) {
            reportBox.clear();
            ObservableList<User> user = User.getAllUsers();
            ObservableList<Appointment> sched = TablePopSQL.getAllAppts();
            user.forEach((u) -> {
            reportBox.appendText("-------------\n" + "Consultant: " + u.getUserName() + "\n");
                sched.forEach((s) -> {
                    if(s.getUser().equals(u.getUserName())) {
                        reportBox.appendText("  Date: " + s.getStartDate().toString() + " from " + 
                                s.getStartTime().toString() +
                                " to " + s.getEndTime().toString() + "\n");
                    }
                });
            });
        }
    }

    @FXML
    private void cbcSelect(ActionEvent event) throws SQLException {
        
        //lamdas to efficiently attach all customers under their appropriate country
        if(cbcTog.isSelected()) {
            reportBox.clear();
            ObservableList<Customer> cust = TablePopSQL.getAllCustomers();
            ObservableList<Country> ctry = Country.getAllCountries();
            ctry.forEach((c) -> {
            reportBox.appendText("-------------\n" + "Country: " + c.getCountry() + "\n");
                cust.forEach((n) -> {
                    if(n.getCountry().equals(c.getCountry())) {
                        reportBox.appendText("  " + n.getCustomerName() + "\n");
                    }
                });
            });
        }
    }   
}
