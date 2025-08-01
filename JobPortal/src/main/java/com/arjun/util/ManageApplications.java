package com.arjun.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ManageApplications {

    // Method to check if the application exists and belongs to the recruiter
    public static boolean checkApplicationExists(int applicationId, int recruiterId) {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT a.id FROM applications a " +
                       "JOIN jobs j ON a.job_id = j.id " +
                       "WHERE a.id = ? AND j.posted_by = ?";
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, applicationId);
            pstmt.setInt(2, recruiterId);
            ResultSet rs = pstmt.executeQuery();
            
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            conn.close();
            return exists;
        } catch (Exception e) {
            System.out.println("⚠ Error checking application.");
            e.printStackTrace();
            return false;
        }
    }

    // Method to update the application status
    public static void updateApplicationStatus(int applicationId, String status) {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE applications SET status = ? WHERE id = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, status); // Use ENUM values: 'accepted' or 'rejected'
            pstmt.setInt(2, applicationId);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Application status updated to: " + status);
            } else {
                System.out.println("❌ Failed to update application status.");
            }

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("⚠ Error updating application status.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Your Email (Recruiter Email): ");
        String recruiterEmail = scanner.nextLine();

        int recruiterId = ViewApplications.getRecruiterIdByEmail(recruiterEmail);

        if (recruiterId == -1) {
            System.out.println("❌ Invalid recruiter email.");
            return;
        }

        System.out.println("Enter Application ID to Manage: ");
        int applicationId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (!checkApplicationExists(applicationId, recruiterId)) {
            System.out.println("❌ No application found with this ID for your jobs.");
            return;
        }

        System.out.println("Choose an action (A for accepted, R for rejected): ");
        String action = scanner.nextLine().toUpperCase(); // Convert to uppercase

        if (action.equals("A")) {
            updateApplicationStatus(applicationId, "accepted"); // ✅ Use full ENUM value
        } else if (action.equals("R")) {
            updateApplicationStatus(applicationId, "rejected"); // ✅ Use full ENUM value
        } else {
            System.out.println("❌ Invalid action. Please enter 'A' for accepted or 'R' for rejected.");
        }

        scanner.close();
    }
}
