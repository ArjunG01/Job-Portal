package com.arjun.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class ViewApplications {

    public static int getRecruiterIdByEmail(String email) {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT id FROM users WHERE email = ?";
        int recruiterId = -1;

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                recruiterId = rs.getInt("id");
            } else {
                System.out.println("❌ No recruiter found with this email.");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("⚠ Error fetching recruiter ID.");
            e.printStackTrace();
        }

        return recruiterId;
    }

    public static void displayApplications(int recruiterId) {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT a.id AS application_id, j.title AS job_title, u.name AS applicant_name, u.email AS applicant_email " +
                       "FROM applications a " +
                       "JOIN jobs j ON a.job_id = j.id " +
                       "JOIN users u ON a.user_id = u.id " +
                       "WHERE j.posted_by = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, recruiterId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n📋 Job Applications for Your Posted Jobs:");
            System.out.println("----------------------------------------------------------");
            boolean hasApplications = false;
            while (rs.next()) {
                hasApplications = true;
                System.out.println("Application ID: " + rs.getInt("application_id"));
                System.out.println("Job Title: " + rs.getString("job_title"));
                System.out.println("Applicant Name: " + rs.getString("applicant_name"));
                System.out.println("Applicant Email: " + rs.getString("applicant_email"));
                System.out.println("----------------------------------------------------------");
            }

            if (!hasApplications) {
                System.out.println("📭 No applications found for your jobs.");
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("⚠ Error fetching applications.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Your Email (Recruiter Email): ");
        String recruiterEmail = scanner.nextLine();

        int recruiterId = getRecruiterIdByEmail(recruiterEmail);

        if (recruiterId != -1) {
            displayApplications(recruiterId);
        } else {
            System.out.println("❌ Invalid recruiter email. Try again.");
        }
    }
}
