package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.arjun.util.DBConnection;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        response.setContentType("text/html");
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        System.out.println("Login attempt: Email=" + email + ", Role=" + role);

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Checking database for matching credentials...");

            String sql = "SELECT id, name, password FROM users WHERE email = ? AND role = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, role);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password").trim();
                int userId = rs.getInt("id");
                String name = rs.getString("name");

                if (storedPassword.equals(password)) {
                    session.setAttribute("user_id", userId);
                    session.setAttribute("email", email);
                    session.setAttribute("role", role);
                    session.setAttribute("name", name);
                    session.setAttribute("user_name", name); // ✅ This is required for ApplyJobServlet

                    System.out.println("Login successful! Redirecting to dashboard...");

                    if ("admin".equals(role)) {
                        response.sendRedirect("admin_dashboard.jsp");
                    } else if ("recruiter".equals(role)) {
                        response.sendRedirect("recruiter_dashboard.jsp");
                    } else {
                        response.sendRedirect("jobseeker_dashboard.jsp");
                    }
                } else {
                    System.out.println("Login failed: Incorrect password.");
                    response.sendRedirect("login.jsp?error=Invalid password.");
                }
            } else {
                System.out.println("Login failed: User not found.");
                response.sendRedirect("login.jsp?error=User not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Server error, try again later.");
        }
    }
}
