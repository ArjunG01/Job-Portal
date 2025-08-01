package com.arjun.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/job_portal?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "@Arjun001@";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database Connected Successfully!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        // Test DB connection
        Connection testConn = getConnection();
        if (testConn != null) {
            System.out.println("🔌 Connection Test Successful.");
        } else {
            System.out.println("❌ Connection Test Failed.");
        }
    }
}
