package com.arjun.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ViewJobs {

    public static void displayJobs() {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT id, title, company, location, salary FROM jobs";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nAvailable Jobs:");
            System.out.println("----------------------------------------------------------");
            while (rs.next()) {
                System.out.println("Job ID: " + rs.getInt("id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Company: " + rs.getString("company"));
                System.out.println("Location: " + rs.getString("location"));
                System.out.println("Salary: ₹" + rs.getDouble("salary"));
                System.out.println("----------------------------------------------------------");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("⚠ Error fetching job listings.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        displayJobs();
    }
}
