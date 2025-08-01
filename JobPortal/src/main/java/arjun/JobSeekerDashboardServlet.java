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

@WebServlet("/JobSeekerDashboardServlet")
public class JobSeekerDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp?error=Please login first.");
            return;
        }

        Integer userId = (Integer) session.getAttribute("user_id");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new Exception("Database connection failed!");
            }

            String sql = "SELECT id, title, company, description, location, salary, recruiter_id FROM jobs";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            ArrayList<Job> jobs = new ArrayList<>();
            while (rs.next()) {
                Job job = new Job(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("company"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getBigDecimal("salary"),  // ✅ Ensure DB column type is DECIMAL
                    rs.getInt("recruiter_id")    // ✅ Recruiter ID added
                );
                jobs.add(job);
            }

            request.setAttribute("jobs", jobs);
            request.getRequestDispatcher("jobseeker_dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jobseeker_dashboard.jsp?error=Server error, try again later.");
        } finally {
            // ✅ Always close resources
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}
