/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatedattendance.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author danle
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/attendance_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Connected to MySQL Database!");
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found!", e);
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
