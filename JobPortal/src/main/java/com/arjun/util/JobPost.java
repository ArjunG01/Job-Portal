package com.arjun.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JobPost {

    // Method to post a job
    public static void postJob(String title, String description, String company, String location, double salary, String recruiterEmail) {
        Connection conn = DBConnection.getConnection();

        try {
            // Fetch Recruiter ID from email
            String recruiterQuery = "SELECT id FROM users WHERE email = ?";
            PreparedStatement pstmt1 = conn.prepareStatement(recruiterQuery);
            pstmt1.setString(1, recruiterEmail);
            ResultSet rs = pstmt1.executeQuery();

            if (rs.next()) {
                int recruiterId = rs.getInt("id");  // Get recruiter ID

                // Insert Job Posting with recruiter ID
                String query = "INSERT INTO jobs (title, description, company, location, salary, posted_by) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(query);
                pstmt2.setString(1, title);
                pstmt2.setString(2, description);
                pstmt2.setString(3, company);
                pstmt2.setString(4, location);
                pstmt2.setDouble(5, salary);
                pstmt2.setInt(6, recruiterId);

                int rowsInserted = pstmt2.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("✅ Job posted successfully!");
                } else {
                    System.out.println("❌ Job posting failed.");
                }

                pstmt2.close();
            } else {
                System.out.println("⚠ Recruiter email not found! Please register first.");
            }

            pstmt1.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("⚠ Error while posting job.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Job Title: ");
        String title = scanner.nextLine();

        System.out.println("Enter Job Description: ");
        String description = scanner.nextLine();

        System.out.println("Enter Company Name: ");
        String company = scanner.nextLine();

        System.out.println("Enter Job Location: ");
        String location = scanner.nextLine();

        System.out.println("Enter Salary: ");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.println("Enter Your Email (Recruiter Email): ");
        String recruiterEmail = scanner.nextLine();

        postJob(title, description, company, location, salary, recruiterEmail);
    }
}
