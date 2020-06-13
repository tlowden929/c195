/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.DBConnect;
import Util.DBQuery;
import Util.DateTime;
import View_Controller.LoginController;
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
public class Country {
    
    //array for country list - reports use only
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    
    //fields
    private int countryId;
    private String country;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    //constructor
    public Country() {
        
    }

    //setters and getters
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
    
    public static void setAllCountries() throws SQLException {
        Connection conn = DBConnect.conn;
        String addCountry = "SELECT country.country FROM country";
        
        DBQuery.setPreparedStatement(conn, addCountry);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        while(rs.next()) {
            Country country = new Country();
            country.setCountry(rs.getString("country"));
            allCountries.add(country);
        }
    }
    
    public static ObservableList<Country> getAllCountries() {
        
        return allCountries;
    }
    
    //statements to check country input and either add or update existing info in DB
    public static void addupdateCountry(String addCountry) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String checkCountry = "SELECT countryId FROM country WHERE country.country = ?";
        String addNewCountry = "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
        "VALUES (?, ?, ?, ?, ?)";
        String updateOldCountry = "UPDATE country SET lastUpdate = ?, lastUpdateBy = ? WHERE country.country = ?";
        
        DBQuery.setPreparedStatement(conn, checkCountry);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, addCountry);
        ps.execute();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        
        ResultSet rs = ps.getResultSet();
        if (rs.next()) {
            DBQuery.setPreparedStatement(conn, updateOldCountry);
            PreparedStatement ps1 = DBQuery.getPreparedStatement();
            ps1.setTimestamp(1, tsNow);
            ps1.setString(2, LoginController.loggedIn.getUserName());
            ps1.setString(3, addCountry);
            ps1.execute();
            }
        else {
            DBQuery.setPreparedStatement(conn, addNewCountry);
            PreparedStatement ps2 = DBQuery.getPreparedStatement();
            ps2.setString(1, addCountry);
            ps2.setTimestamp(2, tsNow);
            ps2.setString(3, LoginController.loggedIn.getUserName());
            ps2.setTimestamp(4, tsNow);
            ps2.setString(5, LoginController.loggedIn.getUserName());
            ps2.execute();
        }  
    }    
}
