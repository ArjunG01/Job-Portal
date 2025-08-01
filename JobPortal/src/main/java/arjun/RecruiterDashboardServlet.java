package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.arjun.util.DBConnection;
import com.arjun.model.Job;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/RecruiterDashboardServlet")
public class RecruiterDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RecruiterDashboardServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); // ✅ Prevents creating a new session
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp?error=Please login first.");
            return;
        }

        // ✅ Ensure user_id is an Integer
        Integer recruiterId;
        try {
            recruiterId = (Integer) session.getAttribute("user_id");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Invalid user_id in session", e);
            response.sendRedirect("login.jsp?error=Session error, please login again.");
            return;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new Exception("Database connection failed!");
            }

            String sql = "SELECT id, title, company, description, location, salary, recruiter_id FROM jobs WHERE recruiter_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, recruiterId);
            rs = stmt.executeQuery();

            ArrayList<Job> jobs = new ArrayList<>();
            while (rs.next()) {
                Job job = new Job(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getBigDecimal("salary"),
                    rs.getInt("recruiter_id")
                );
                jobs.add(job);
            }

            request.setAttribute("jobs", jobs);
            request.getRequestDispatcher("recruiter_dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database error in RecruiterDashboardServlet", e);
            request.setAttribute("error", "Server error, please try again later.");
            request.getRequestDispatcher("recruiter_dashboard.jsp").forward(request, response);
        } finally {
            // ✅ Always close resources safely
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
