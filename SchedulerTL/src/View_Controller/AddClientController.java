/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Address;
import Model.City;
import Model.Country;
import Model.Customer;
import Util.TablePopSQL;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ten
 */
public class AddClientController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private TextField lastNametxt;
    @FXML
    private TextField firstNametxt;
    @FXML
    private TextField address1txt;
    @FXML
    private TextField address2txt;
    @FXML
    private TextField postalCodetxt;
    @FXML
    private TextField citytxt;
    @FXML
    private TextField countrytxt;
    @FXML
    private TextField phonetxt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addClientbtn(ActionEvent event) throws SQLException {
        
        boolean validCustomer = Customer.isNull(lastNametxt.getText(), firstNametxt.getText(), address1txt.getText(), address2txt.getText(),
                postalCodetxt.getText(), citytxt.getText(), countrytxt.getText(), phonetxt.getText());
        
        if(validCustomer != false) {
            Alert nullFields = new Alert(Alert.AlertType.WARNING,
                "Please enter valid date for all fields.",
                ButtonType.OK);
                nullFields.showAndWait();
            }
        else {
            //adds new customer text fields to the appropriate DB tables and then adds the values to the tableview        
            try {
                String customerConcat = lastNametxt.getText() + ", " + firstNametxt.getText();
            
                Country.addupdateCountry(countrytxt.getText());
                City.addupdateCity(citytxt.getText(), countrytxt.getText());
                Address.addupdateAddress(address1txt.getText(), address2txt.getText(), citytxt.getText(), postalCodetxt.getText(), phonetxt.getText());
                Customer.addNewCustomer(customerConcat, address1txt.getText(), address2txt.getText());
            
                TablePopSQL.setAllCustomers();
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View_Controller/ClientList.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                } 
            catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelAddCbtn(ActionEvent event) throws IOException, SQLException {
        
        Alert Cancel = new Alert(Alert.AlertType.CONFIRMATION, 
        "Cancel and return to the previous page? No changes will be saved.",
        ButtonType.YES,
        ButtonType.NO);
        Cancel.setTitle("Cancel?");
        Optional<ButtonType> result = Cancel.showAndWait();
            if(result.get() == ButtonType.YES) {
            TablePopSQL.setAllCustomers();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View_Controller/ClientList.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else if(result.get() == ButtonType.NO) {
            event.consume();
        }
    }
}
