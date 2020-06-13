/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import View_Controller.LoginController;
import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 *
 * @author Ten
 */
public class DateTime {
    
    private static ZoneId localZone = ZoneId.systemDefault();
    
    public static LocalDateTime UTCtoZDT(Timestamp timestamp) {
        
        LocalDateTime ldt = timestamp.toLocalDateTime();
        ZonedDateTime setUTC = ldt.atZone(ZoneId.of("UTC"));
        LocalDateTime ldtIn = setUTC.toLocalDateTime();
        
        ZonedDateTime zdtOut = ldtIn.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtOutToLocalTZ = zdtOut.withZoneSameInstant(ZoneId.of(localZone.toString()));
        LocalDateTime zdtOutFinal = zdtOutToLocalTZ.toLocalDateTime();
        
        return zdtOutFinal;
    }
    
    public static LocalDateTime ZDTtoUTC(LocalDateTime localdatetime) {
        
        ZonedDateTime setZDT = localdatetime.atZone(ZoneId.of(localZone.toString()));
        LocalDateTime zdtIn = setZDT.toLocalDateTime();
        
        ZonedDateTime utcOut = zdtIn.atZone(ZoneId.of(localZone.toString()));
        ZonedDateTime utcOutToUTC = utcOut.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcOutFinal = utcOutToUTC.toLocalDateTime();
        
        return utcOutFinal;
    }
    
    public static boolean timeOverlap(LocalDateTime start, LocalDateTime end, int apptId) throws SQLException {
        
        boolean overlap = false;
        LocalDateTime utcStart = ZDTtoUTC(start);
        LocalDateTime utcEnd = ZDTtoUTC(end);
        
        Connection conn = DBConnect.conn;
        String selectOverlap = "SELECT appointment.start, appointment.end FROM appointment \n" +
        "WHERE (appointment.start BETWEEN ? and ?" +
        "OR appointment.end BETWEEN ? and ?) AND userId = ? AND appointmentId != ?";
        DBQuery.setPreparedStatement(conn, selectOverlap);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setTimestamp(1, Timestamp.valueOf(utcStart));
        ps.setTimestamp(2, Timestamp.valueOf(utcEnd));
        ps.setTimestamp(3, Timestamp.valueOf(utcStart));
        ps.setTimestamp(4, Timestamp.valueOf(utcEnd));
        ps.setInt(5, LoginController.loggedIn.getUserId());
        ps.setInt(6, apptId);
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        if(rs.next()) {
            overlap = true;
        }
    return overlap;
    }
}