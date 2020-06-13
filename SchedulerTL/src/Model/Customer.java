/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Util.DBConnect;
import Util.DBQuery;
import Util.DateTime;
import Util.TablePopSQL;
import View_Controller.LoginController;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author Ten
 */
public class Customer {
    
    //fields
    private int customerId;
    private String customerName;
    private int addressId;
    private boolean active;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private String address;
    private String address2;
    private String phone;
    private String city;
    private String postalCode;
    private String country;
    
    //constructor
    public Customer() {         
    }

    //setters and getters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    //statements to check if customer exists in DB
    public static String[] searchCustomer(String customerName) throws SQLException {
    
        String[] isCustomer = null;
        for (Customer customer : TablePopSQL.getAllCustomers()) {
            if (customer.getCustomerName().toLowerCase().contains(customerName.toLowerCase())){
                isCustomer = new String[2];
                isCustomer[0] = customer.getCustomerName();
                isCustomer[1] = customer.getPhone();
            }
        }   
        return isCustomer;
    }
        
    //statements to add new customer to DB
    public static void addNewCustomer (String addCustomer, String addAddress, String addAddress2) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String customerAdd = "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
        "VALUES (?, (SELECT address.addressId FROM address WHERE address.address = ? AND address.address2 = ?),\n" +
        "?, ?, ?, ?, ?)";
        DBQuery.setPreparedStatement(conn, customerAdd);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        
        ps.setString(1, addCustomer);
        ps.setString(2, addAddress);
        ps.setString(3, addAddress2); 
        ps.setInt(4, 1);
        ps.setTimestamp(5, tsNow);
        ps.setString(6, LoginController.loggedIn.getUserName());
        ps.setTimestamp(7, tsNow);
        ps.setString(8, LoginController.loggedIn.getUserName());
        ps.execute();
    }
    
    //statements to update existing customer info in DB\
    public static void updateCustomer (String updateCustomer, String oldCustomer, String address, String address2) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String customerUpdate = "UPDATE customer SET customer.customerName = ?,\n" +
        "addressId = (SELECT address.addressId FROM address WHERE address.address = ? AND address.address2 = ?),\n" +
        "lastUpdate = ?, lastUpdateBy = ? WHERE customerName = ?";
        DBQuery.setPreparedStatement(conn, customerUpdate);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        
        ps.setString(1, updateCustomer);
        ps.setString(2, address);
        ps.setString(3, address2);
        ps.setTimestamp(4, tsNow);
        ps.setString(5, LoginController.loggedIn.getUserName());
        ps.setString(6, oldCustomer);
        ps.execute();
    }
    
    //statements to delete customer from DB
    public static void deleteCustomer (Customer selectedCustomer) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String customerDel = "DELETE FROM customer WHERE customerName = ?";
        DBQuery.setPreparedStatement(conn, customerDel);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, selectedCustomer.getCustomerName());
        ps.execute();
        
        TablePopSQL.allCustomers.remove(selectedCustomer);   
    }
    
    //statement to remove appointment linked to customer on delete
    public static void deleteLinkedAppt (Customer linkedCustomer) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String linkApptDel = "DELETE FROM appointment WHERE appointment.customerId = (SELECT customerId FROM customer\n" +
        "WHERE customerName = ?)";
        DBQuery.setPreparedStatement(conn, linkApptDel);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, linkedCustomer.getCustomerName());
        ps.execute();   
    }

    public static boolean isNull(String lastname, String firstname, String address1, String address2, String postal, 
            String city, String country, String phone) {
        
        boolean blank = false;
        if(lastname.isEmpty() || lastname == null) {
         blank = true;   
        }
        if(firstname.isEmpty() || firstname == null) {
            blank = true;
        }
        if(address1.isEmpty() || address1 == null) {
            blank = true;
        }
        if(address2.isEmpty() || address2 == null) {
            blank = true;
        }
        if(postal.isEmpty() || postal == null) {
            blank = true;
        }
        if(city.isEmpty() || city == null) {
            blank = true;
        }
        if(country.isEmpty() || country == null) {
            blank = true;
        }
        if(phone.isEmpty() || phone == null) {
            blank = true;
        }
        return blank;
    }
}
