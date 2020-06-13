/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import com.mysql.jdbc.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Ten
 */
public class DBQuery {
    
    private static PreparedStatement statement;
    
    //Statement Creation
    public static void setPreparedStatement(Connection conn, String sqlStmt) throws SQLException {
        
        statement = conn.prepareStatement(sqlStmt);
        
    }
    
    //Statement Return
    public static PreparedStatement getPreparedStatement(){
        
        return statement;
    }
    
}
