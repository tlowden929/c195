/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Model.Appointment;
import Model.Customer;
import View_Controller.LoginController;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ten
 */
public class TablePopSQL {
    
    //arrays to display appointments
    private static ObservableList<Appointment> allAppts = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weekAppts = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthAppts = FXCollections.observableArrayList();
    
    //array to display customers
    public static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    
    //sets all appointments and the users assigned to them
    public static void setAllAppts() throws SQLException {
        
        Connection conn = DBConnect.conn;
        String selectAppts = "SELECT customerName, appointmentId, appointment.userId, user.userName,\n" +
        "title, description, location, contact, type, url, start, end\n" +
        "FROM customer JOIN appointment ON customer.customerId = appointment.customerId\n" +
        "JOIN user ON appointment.userId = user.userId ORDER BY start";
        
        DBQuery.setPreparedStatement(conn, selectAppts);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        while(rs.next()) {
               
            LocalDateTime startTime = DateTime.UTCtoZDT(rs.getTimestamp("start"));
            LocalDate sdate = startTime.toLocalDate();
            LocalTime stime = startTime.toLocalTime();
            LocalDateTime endTime = DateTime.UTCtoZDT(rs.getTimestamp("end"));
            LocalDate edate = endTime.toLocalDate();
            LocalTime etime = endTime.toLocalTime();
                       
            Appointment currAppt = new Appointment();
            currAppt.setUserId(rs.getInt("userId"));
            currAppt.setAppointmentId(rs.getInt("appointmentId"));
            currAppt.setTitle(rs.getString("title"));
            currAppt.setDescription(rs.getString("description"));
            currAppt.setLocation(rs.getString("location"));
            currAppt.setContact(rs.getString("contact"));
            currAppt.setType(rs.getString("type"));
            currAppt.setUrl(rs.getString("url"));
            currAppt.setStartDate(sdate);
            currAppt.setEndDate(edate);
            currAppt.setStartTime(stime);
            currAppt.setEndTime(etime);
            currAppt.setCustomer(rs.getString("customerName"));
            currAppt.setUser(rs.getString("userName"));
            allAppts.add(currAppt);
        }    
    }
        
    //sets all weekly appointments filtered by userId logged in
    public static void setWeekAppts() throws SQLException {
            
        Connection conn = DBConnect.conn;
        String selectStatement = "SELECT customerName, appointmentId, appointment.userId,\n" +
        "title, description, location, contact, type, url, start, end\n" +
        "FROM customer JOIN appointment ON customer.customerId = appointment.customerId\n" +
        "WHERE appointment.start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY) \n" +
        "AND appointment.userId = ? ORDER BY start";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, LoginController.loggedIn.getUserId());
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
            
        while(rs.next()) {
           
            LocalDateTime startTime = DateTime.UTCtoZDT(rs.getTimestamp("start"));
            LocalDate sdate = startTime.toLocalDate();
            LocalTime stime = startTime.toLocalTime();
            LocalDateTime endTime = DateTime.UTCtoZDT(rs.getTimestamp("end"));
            LocalDate edate = endTime.toLocalDate();
            LocalTime etime = endTime.toLocalTime();
                
            Appointment currAppt = new Appointment();
            currAppt.setUserId(rs.getInt("userId"));
            currAppt.setAppointmentId(rs.getInt("appointmentId"));
            currAppt.setTitle(rs.getString("title"));
            currAppt.setDescription(rs.getString("description"));
            currAppt.setLocation(rs.getString("location"));
            currAppt.setContact(rs.getString("contact"));
            currAppt.setType(rs.getString("type"));
            currAppt.setUrl(rs.getString("url"));
            currAppt.setStartDate(sdate);
            currAppt.setEndDate(edate);
            currAppt.setStartTime(stime);
            currAppt.setEndTime(etime);
            currAppt.setCustomer(rs.getString("customerName"));
            weekAppts.add(currAppt);
        }                               
    }
        
    //sets all monthly appointments filtered by userId logged in
    public static void setMonthAppts() throws SQLException {
            
        Connection conn = DBConnect.conn;
        String selectStatement = "SELECT customerName, appointmentId, appointment.userId,\n" +
        "title, description, location, contact, type, url, start, end\n" +
        "FROM customer JOIN appointment ON customer.customerId = appointment.customerId\n" +
        "WHERE appointment.start BETWEEN NOW() AND LAST_DAY(NOW()) \n" +
        "AND appointment.userId = ? ORDER BY start";
        DBQuery.setPreparedStatement(conn, selectStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, LoginController.loggedIn.getUserId());
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
            
        while(rs.next()) {
                
            LocalDateTime startTime = DateTime.UTCtoZDT(rs.getTimestamp("start"));
            LocalDate sdate = startTime.toLocalDate();
            LocalTime stime = startTime.toLocalTime();
            LocalDateTime endTime = DateTime.UTCtoZDT(rs.getTimestamp("end"));
            LocalDate edate = endTime.toLocalDate();
            LocalTime etime = endTime.toLocalTime();
                
            Appointment currAppt = new Appointment();
            currAppt.setUserId(rs.getInt("userId"));
            currAppt.setAppointmentId(rs.getInt("appointmentId"));
            currAppt.setTitle(rs.getString("title"));
            currAppt.setDescription(rs.getString("description"));
            currAppt.setLocation(rs.getString("location"));
            currAppt.setContact(rs.getString("contact"));
            currAppt.setType(rs.getString("type"));
            currAppt.setUrl(rs.getString("url"));
            currAppt.setStartDate(sdate);
            currAppt.setEndDate(edate);
            currAppt.setStartTime(stime);
            currAppt.setEndTime(etime);
            currAppt.setCustomer(rs.getString("customerName"));
            monthAppts.add(currAppt);
        }    
    }
    
    //sets all customers and corresponding address and contact info
    public static void setAllCustomers() throws SQLException {
        
        Connection conn = DBConnect.conn;
        String selectCustomers = "SELECT customer.customerName, address.address, address.address2, address.phone, address.postalCode,\n" +
        "city.city, country.country FROM customer\n" +
        "JOIN address ON customer.addressId = address.addressId\n" +
        "JOIN city ON address.cityId = city.cityId\n" +
        "JOIN country ON city.countryId = country.countryId ORDER BY country.country";
        
        DBQuery.setPreparedStatement(conn, selectCustomers);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        while (rs.next()) {
            
            Customer currCustomer = new Customer();
            currCustomer.setCustomerName(rs.getString("customerName"));
            currCustomer.setAddress(rs.getString("address"));
            currCustomer.setAddress2(rs.getString("address2"));
            currCustomer.setCity(rs.getString("city"));
            currCustomer.setPostalCode(rs.getString("postalCode"));
            currCustomer.setCountry(rs.getString("country"));
            currCustomer.setPhone(rs.getString("phone"));
            allCustomers.add(currCustomer);
        }
    }
    
    //getters
    public static ObservableList<Appointment> getAllAppts() {
        
        return allAppts;
    }
    
    public static ObservableList<Appointment> getWeekAppts() {
        
        return weekAppts;
    }
    
    public static ObservableList<Appointment> getMonthAppts() {
        
        return monthAppts;
    }
    
    public static ObservableList<Customer> getAllCustomers() {
        
        return allCustomers;
    }           
}
