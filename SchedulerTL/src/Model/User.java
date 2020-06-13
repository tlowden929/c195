/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.DBConnect;
import Util.DBQuery;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ten
 */
public class User {

    //array for list of Users
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    
    //fields
    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    //empty constructor
    public User() {    
    }
    
    //constructor to set logged-in user
    public User(String name, int id) {
        this.userName = name;
        this.userId = id;
    }

    //setters and getters
    public int getUserId() {
        return userId;
    }
   
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    //adds current users from DB to list for login comparison
    public static void setUsers() throws SQLException {
        
        Connection conn = DBConnect.conn;
        String addUsers = "SELECT userId, userName, password, active FROM user";
        
        DBQuery.setPreparedStatement(conn, addUsers);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setActive(rs.getBoolean("active"));
            allUsers.add(user);
        }
    }
    
     public static ObservableList<User> getAllUsers() {
         
         return allUsers;
     }
    
    //compares login fields to data from allUsers and returns userId to set the appt by user
    public static int loginUser(String userName, String password) throws SQLException {
        int loginId = 0;
        for (User user : allUsers) {
            if ((user.getUserName().equals(userName)) && (user.getPassword().equals(password))){
                loginId = user.getUserId();
            }
        }    
        return loginId;      
    }          
}
