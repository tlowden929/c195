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


/**
 *
 * @author Ten
 */
public class City {
    
    //fields
    private int cityId;
    private String city;
    private int countryId;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    //constructor
    public City() {
        
    }

    //setters and getters
    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
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
    
   
    //statements to check city input and either add or update existing info in DB
    public static void addupdateCity(String addCity, String addCountry) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String checkCity = "SELECT cityId FROM city WHERE city.city = ?";
        String addNewCity = "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
        "VALUES (?, (SELECT country.countryId FROM country WHERE country.country = ?),\n" +
        "?, ?, ?, ?)";
        String updateOldCity = "UPDATE city SET lastUpdate = ?, lastUpdateBy = ? WHERE city.city = ?";
        
        DBQuery.setPreparedStatement(conn, checkCity);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, addCity);
        ps.execute();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        
        ResultSet rs = ps.getResultSet();
        if (rs.next()) {
            DBQuery.setPreparedStatement(conn, updateOldCity);
            PreparedStatement ps1 = DBQuery.getPreparedStatement();
            ps1.setTimestamp(1, tsNow);
            ps1.setString(2, LoginController.loggedIn.getUserName());
            ps1.setString(3, addCity);
            ps1.execute();
            }
        else {
            DBQuery.setPreparedStatement(conn, addNewCity);
            PreparedStatement ps2 = DBQuery.getPreparedStatement();
            ps2.setString(1, addCity);
            ps2.setString(2, addCountry); 
            ps2.setTimestamp(3, tsNow);
            ps2.setString(4, LoginController.loggedIn.getUserName());
            ps2.setTimestamp(5, tsNow);
            ps2.setString(6, LoginController.loggedIn.getUserName());
            ps2.execute();
        }
        
    }
        
}
