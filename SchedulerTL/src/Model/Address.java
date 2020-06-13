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
public class Address {
    
    //fields
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    //constructor
    public Address() {
        
    }

    //setters and getters
    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
    
    //statements to check address input and either add or update existing info in DB
    public static void addupdateAddress(String addAddress, String addAddress2, String addCity, String addZip, String addPhone) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String checkAddress = "SELECT addressId FROM address WHERE address.address = ? AND address.address2 = ?";
        String addNewAddress = "INSERT INTO address (address, address2, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy, cityId)\n" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, (SELECT city.cityId FROM city WHERE city.city = ?))";
        String updateOldAddress = "UPDATE address SET lastUpdate = ?, lastUpdateBy = ? WHERE address.address = ? AND address.address2 = ?";
        
        DBQuery.setPreparedStatement(conn, checkAddress);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, addAddress);
        ps.setString(2, addAddress2);
        ps.execute();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        
        ResultSet rs = ps.getResultSet();
        if (rs.next()) {
            DBQuery.setPreparedStatement(conn, updateOldAddress);
            PreparedStatement ps1 = DBQuery.getPreparedStatement();
            ps1.setTimestamp(1, tsNow);
            ps1.setString(2, LoginController.loggedIn.getUserName());
            ps1.setString(3, addAddress);
            ps1.setString(4, addAddress2);
            ps1.execute();
            }
        else {
            DBQuery.setPreparedStatement(conn, addNewAddress);
            PreparedStatement ps2 = DBQuery.getPreparedStatement();
            ps2.setString(1, addAddress);
            ps2.setString(2, addAddress2);
            ps2.setString(3, addZip); 
            ps2.setString(4, addPhone);
            ps2.setTimestamp(5, tsNow);
            ps2.setString(6, LoginController.loggedIn.getUserName());
            ps2.setTimestamp(7, tsNow);
            ps2.setString(8, LoginController.loggedIn.getUserName());
            ps2.setString(9, addCity);
            ps2.execute();
        }
    }
        
}
