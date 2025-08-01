package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.arjun.util.DBConnection;
import com.arjun.model.User;
import com.arjun.model.Job;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("admin")) {
            response.sendRedirect("login.jsp?error=Access Denied.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Fetch all users
            String userSql = "SELECT * FROM users";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            ResultSet userRs = userStmt.executeQuery();

            ArrayList<User> users = new ArrayList<>();
            while (userRs.next()) {
                User user = new User(
                    userRs.getInt("id"),
                    userRs.getString("name"), // ✅ Fixed column name (use "name" instead of "full_name")
                    userRs.getString("email"),
                    userRs.getString("role")
                );
                users.add(user);
            }

            // Fetch all jobs
            String jobSql = "SELECT * FROM jobs";
            PreparedStatement jobStmt = conn.prepareStatement(jobSql);
            ResultSet jobRs = jobStmt.executeQuery();

            ArrayList<Job> jobs = new ArrayList<>();
            while (jobRs.next()) {
                Job job = new Job(
                    jobRs.getInt("id"),
                    jobRs.getString("title"),
                    jobRs.getString("company"),
                    jobRs.getString("description"),
                    jobRs.getString("location"),
                    jobRs.getBigDecimal("salary"), // ✅ Fixed salary type
                    jobRs.getInt("recruiter_id")   // ✅ Added recruiter_id
                );
                jobs.add(job);
            }

            request.setAttribute("users", users);
            request.setAttribute("jobs", jobs);
            request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.jsp?error=Server error, try again later.");
        }
    }
}
