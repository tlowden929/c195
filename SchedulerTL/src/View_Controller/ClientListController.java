/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
import Util.TablePopSQL;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
public class ClientListController implements Initializable {
    Stage stage;
    Parent scene;
    
    @FXML
    private TableView<Customer> customerTbl;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> address2Col;
    @FXML
    private TableColumn<Customer, String> cityCol;
    @FXML
    private TableColumn<Customer, String> postalCodeCol;
    @FXML
    private TableColumn<Customer, String> countryCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
            
            //sets all customers listed in the DB to the tableview
            customerTbl.refresh();
            customerTbl.setItems(TablePopSQL.getAllCustomers());

            nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            address2Col.setCellValueFactory(new PropertyValueFactory<>("address2"));
            cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
            postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            
            nameCol.setSortType(TableColumn.SortType.ASCENDING);
            customerTbl.getSortOrder().add(nameCol);
            customerTbl.sort();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }    

    @FXML
    private void addCustomerbtn(ActionEvent event) throws IOException {
        
        customerTbl.getItems().clear();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/AddClient.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void editCustomerbtn(ActionEvent event) throws IOException {
        
        Customer editCustomer = customerTbl.getSelectionModel().getSelectedItem();
        
        if(editCustomer != null) {
            Parent root;
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/EditClient.fxml"));
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            EditClientController controller = loader.getController();
            controller.setCustomer(editCustomer);
            customerTbl.getItems().clear();
            }
        else {
            Alert selectCustomer = new Alert(AlertType.WARNING,
                "Please select a client to edit!",
                ButtonType.OK);
                selectCustomer.showAndWait();
        }   
    }

    @FXML
    private void delCustomerbtn(ActionEvent event) throws SQLException {
        
        Customer deleteCustomer = customerTbl.getSelectionModel().getSelectedItem();
        
        if(deleteCustomer != null) {
            Alert delCustomer = new Alert(Alert.AlertType.CONFIRMATION, 
            "Are you sure you want to delete the selected client?",
            ButtonType.YES,
            ButtonType.NO);
            delCustomer.setTitle("Delete Client?");
            Optional<ButtonType> result = delCustomer.showAndWait();
                if(result.get() == ButtonType.YES) {
                    Customer.deleteLinkedAppt(deleteCustomer);
                    Customer.deleteCustomer(deleteCustomer);
                    customerTbl.refresh();
                }
                else if(result.get() == ButtonType.NO) {
                event.consume();
                }
            }
        else {
            Alert selectCustomer = new Alert(AlertType.WARNING,
            "Please select a client to delete!",
            ButtonType.OK);
            selectCustomer.showAndWait();        
        }     
    }

    @FXML
    private void backbtn(ActionEvent event) throws IOException, SQLException {
        
        TablePopSQL.setAllAppts();
        TablePopSQL.setWeekAppts();
        TablePopSQL.setMonthAppts();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/View_Controller/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }    
}
