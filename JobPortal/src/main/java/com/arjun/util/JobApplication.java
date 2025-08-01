package com.arjun.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class JobApplication {

    public static void applyForJob(int jobId, int userId) {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO applications (job_id, user_id) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, userId);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Job application submitted successfully!");
            } else {
                System.out.println("❌ Job application failed.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("⚠ Error while applying for the job.");
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Job ID to Apply: ");
        int jobId = scanner.nextInt();

        System.out.println("Enter Your User ID: ");
        int userId = scanner.nextInt();

        applyForJob(jobId, userId);
    }
}
