package arjun;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // ✅ Collect form data (Address field removed)
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String role = request.getParameter("role");

        // ✅ Debugging - Print received values
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);

        // ✅ Basic Validation
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            name == null || name.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            
            out.write("<h3 style='color:red;'>Invalid Input! Please enter valid details.</h3>");
            return;
        }

        // ✅ Email Format Validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            out.write("<h3 style='color:red;'>Invalid email format.</h3>");
            return;
        }

        // ✅ Database Connection Details (Update as per your DB)
        String jdbcURL = "jdbc:mysql://localhost:3306/job_portal"; // Change database name if needed
        String dbUser = "root";  // Change to your DB username
        String dbPassword = "@Arjun001@";  // Change to your DB password

        try {
            // ✅ Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ Establish Connection & Insert User Data (Address removed from query)
            try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)")) {

                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password); // ✅ Plain text password
                stmt.setString(4, role);

                // ✅ Execute Query
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    out.write("<h3 style='color:green;'>Registration successful! You can now log in.</h3>");
                } else {
                    out.write("<h3 style='color:red;'>Registration failed. Please try again.</h3>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.write("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
