/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ten
 */
public class DBConnect {
    
    //URL Parts
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/U05lYm";
    
    //Total URL
    private static final String dbURL = protocol + vendor + ipAddress;
    
    //Driver and Connection Interface
    private static final String MYSQLDriver = "com.mysql.jdbc.Driver";
    public static Connection conn = null;
    
    //Login
    private static final String userName = "U05lYm";
    private static final String passWord = "53688540835";
    
    
    public static Connection startConn () {
        try {
        Class.forName(MYSQLDriver);
        conn = (Connection)DriverManager.getConnection(dbURL, userName, passWord);
        System.out.println("Connection Successful");
        }
        catch(ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static void closeConn() throws SQLException {
        conn.close();
        System.out.println("Connection Closed");
    }
     
}
