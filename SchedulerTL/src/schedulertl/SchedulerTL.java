/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulertl;

import Util.DBConnect;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Ten
 */
public class SchedulerTL extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/View_Controller/Login.fxml"));

        Scene scene = new Scene(root);
        scene.setRoot(root);
        
        primaryStage.setTitle("Appointment Desk");
        primaryStage.setScene(scene);
        primaryStage.show();    
    }
    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
        
        DBConnect.startConn();
        launch(args);
        DBConnect.closeConn();   
    }    
}
