package com.arjun.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserAuth {

    // Method for User Registration
    public static void registerUser(String name, String email, String password, String role) {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, role);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Registration failed.");
            }

            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error during registration!");
            e.printStackTrace();
        }
    }

    // Method for User Login
    public static boolean loginUser(String email, String password) {
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login Successful! Welcome, " + rs.getString("name"));
                return true;
            } else {
                System.out.println("Invalid email or password!");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error during login!");
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option: \n1. Register \n2. Login");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            System.out.println("Enter Name: ");
            String name = scanner.nextLine();

            System.out.println("Enter Email: ");
            String email = scanner.nextLine();

            System.out.println("Enter Password: ");
            String password = scanner.nextLine();

            System.out.println("Enter Role (admin/recruiter/job_seeker): ");
            String role = scanner.nextLine();

            registerUser(name, email, password, role);

        } else if (choice == 2) {
            System.out.println("Enter Email: ");
            String email = scanner.nextLine();

            System.out.println("Enter Password: ");
            String password = scanner.nextLine();

            loginUser(email, password);
        } else {
            System.out.println("Invalid choice! Exiting...");
        }
        
        scanner.close();
    }
}
