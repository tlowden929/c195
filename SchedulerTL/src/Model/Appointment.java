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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 *
 * @author Ten
 */
public class Appointment {
    
    //lists for all suggested available business hour start and end times 8am - 6pm (combobox is also editable)
    public static ObservableList<String> allStartTimes = FXCollections.observableArrayList(
        "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00");
    public static ObservableList<String> allEndTimes = FXCollections.observableArrayList(
        "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");
    
    //fields
    private int appointmentId;
    private int customerId;
    private int userId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private LocalDate startDate;    
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    
    private String customername;
    private String username;

    
    //empty constructor
    public Appointment() {    
    }

    //setters and getters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startdate) {
        this.startDate = startdate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate enddate) {
        this.endDate = enddate;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime starttime) {
        this.startTime = starttime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endtime) {
        this.endTime = endtime;
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
    
    public String getCustomer() {
        
        return customername;
    }
    
    public void setCustomer(String customer) {
        
        this.customername = customer;
    }
    
    public String getUser() {
        
        return username;
    }
    
    public void setUser(String user) {
        
        this.username = user;
    }
    
    //adds appointment info from labeled text fields to DB
    public static void addAppointment(String customer, String title, String description, String location, String contact, String type,
        String url, LocalDateTime start, LocalDateTime end) throws SQLException {
            
        Connection conn = DBConnect.conn;
        String addAppt = "INSERT INTO appointment (customerId, userId, title, description, \n" +
        "location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)\n" +
        "VALUES ((SELECT customer.customerId FROM customer WHERE customer.customerName = ?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        DBQuery.setPreparedStatement(conn, addAppt);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        LocalDateTime tsStart = DateTime.ZDTtoUTC(start);
        LocalDateTime tsEnd = DateTime.ZDTtoUTC(end);
        
        ps.setString(1, customer);
        ps.setInt(2, LoginController.loggedIn.getUserId());
        ps.setString(3, title);
        ps.setString(4, description);
        ps.setString(5, location);
        ps.setString(6, contact);
        ps.setString(7, type);
        ps.setString(8, url);
        ps.setTimestamp(9, Timestamp.valueOf(tsStart));
        ps.setTimestamp(10, Timestamp.valueOf(tsEnd));
        ps.setTimestamp(11, tsNow);
        ps.setString(12, LoginController.loggedIn.getUserName());
        ps.setTimestamp(13, tsNow);
        ps.setString(14, LoginController.loggedIn.getUserName());
        ps.execute();      
    }
    
    //deletes appointments from DB and resets arrays for tableviews
    public static void delAppt(Appointment selectedAppt) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String apptDel = "DELETE FROM appointment WHERE appointmentId = ?";
        DBQuery.setPreparedStatement(conn, apptDel);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, selectedAppt.getAppointmentId());
        ps.execute();
        
        TablePopSQL.setAllAppts();
        TablePopSQL.setMonthAppts();
        TablePopSQL.setWeekAppts();
    }
    
    //updates appointments in DB
    public static void updateAppt(String title, String description, String location, String contact, String type,
            String url, LocalDateTime start, LocalDateTime end, int apptId) throws SQLException {
        
        Connection conn = DBConnect.conn;
        String updAppt = "UPDATE appointment SET title = ?, description = ?, location = ?, contact = ?, type = ?,\n" +
        "url = ?, start = ?, end = ?, lastUpdate = ?, lastUpdateBy = ? WHERE appointmentId = ?";
        DBQuery.setPreparedStatement(conn, updAppt);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        LocalDateTime ldNow = DateTime.ZDTtoUTC(LocalDateTime.now());
        Timestamp tsNow = Timestamp.valueOf(ldNow);
        LocalDateTime tsStart = DateTime.ZDTtoUTC(start);
        LocalDateTime tsEnd = DateTime.ZDTtoUTC(end);
        
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, contact);
        ps.setString(5, type);
        ps.setString(6, url);
        ps.setTimestamp(7, Timestamp.valueOf(tsStart));
        ps.setTimestamp(8, Timestamp.valueOf(tsEnd));
        ps.setTimestamp(9, tsNow);
        ps.setString(10, LoginController.loggedIn.getUserName());
        ps.setInt(11, apptId);
        ps.execute();
    }
    
    public static boolean isNull(String title, String customer, String description, String location, String contact, String type,
            String url, LocalDate start, String stime, String etime) {
        
        boolean blank = false;
        if(title.isEmpty() || title == null) {
            blank = true;
        }
        if(customer.isEmpty() || customer == null) {
            blank = true;
        }
        if(description.isEmpty() || description == null) {
            blank = true;
        } 
        if(location.isEmpty() || location == null) {
            blank = true;
        }
        if(contact.isEmpty() || contact == null) {
            blank = true;
        }
        if(type.isEmpty() || type == null) {
            blank = true;
        }
        if(url.isEmpty() || url == null) {
            blank = true;
        }
        if(start == null) {
            blank = true;
        }
        if(stime.isEmpty() || stime == null) {
            blank = true;
        }
        if(etime.isEmpty()  || etime == null) {
            blank = true;
        }
        return blank;
    }
}
