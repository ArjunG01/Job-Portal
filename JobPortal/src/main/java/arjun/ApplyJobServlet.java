package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.arjun.util.DBConnection;

@WebServlet("/ApplyJobServlet")
public class ApplyJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("user_id");
        String applicantName = (String) session.getAttribute("name"); // ✅ get user name from session

        if (userId == null || applicantName == null || applicantName.trim().isEmpty()) {
            response.sendRedirect("login.jsp?error=Please login first.");
            return;
        }

        String jobIdStr = request.getParameter("job_id");

        if (jobIdStr == null || jobIdStr.trim().isEmpty()) {
            response.sendRedirect("jobseeker_dashboard.jsp?error=Invalid job selection.");
            return;
        }

        try {
            int jobId = Integer.parseInt(jobIdStr.trim());

            try (Connection conn = DBConnection.getConnection()) {

                // ✅ Optional: Check if already applied to prevent duplicate entries
                String checkSql = "SELECT COUNT(*) FROM applications WHERE job_id = ? AND user_id = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setInt(1, jobId);
                    checkStmt.setInt(2, userId);
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next() && rs.getInt(1) > 0) {
                        response.sendRedirect("jobseeker_dashboard.jsp?error=You have already applied for this job.");
                        return;
                    }
                }

                // ✅ Insert the new application with applicant_name
                String sql = "INSERT INTO applications (job_id, user_id, applicant_name, status) VALUES (?, ?, ?, 'applied')";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, jobId);
                    stmt.setInt(2, userId);
                    stmt.setString(3, applicantName); // ✅ Required fix

                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        response.sendRedirect("jobseeker_dashboard.jsp?success=Application submitted.");
                    } else {
                        response.sendRedirect("jobseeker_dashboard.jsp?error=Failed to apply.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("jobseeker_dashboard.jsp?error=Invalid job ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("jobseeker_dashboard.jsp?error=Server error, try again later.");
        }
    }
}
