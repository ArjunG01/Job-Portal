package arjun;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.arjun.util.DBConnection;

@WebServlet("/PostJobServlet")  // ✅ Defines the servlet without web.xml
public class PostJobServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp?error=Please login first.");
            return;
        }

        int recruiterId = (int) session.getAttribute("user_id"); // Get recruiter ID from session
        String title = request.getParameter("title");
        String company = request.getParameter("company");
        String location = request.getParameter("location");
        String description = request.getParameter("description");
        String salaryStr = request.getParameter("salary");

        BigDecimal salary = null;
        if (salaryStr != null && !salaryStr.isEmpty()) {
            try {
                salary = new BigDecimal(salaryStr);
            } catch (NumberFormatException e) {
                response.sendRedirect("recruiter_dashboard.jsp?error=Invalid salary format.");
                return;
            }
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO jobs (title, company, location, salary, description, posted_by) VALUES (?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, title);
            stmt.setString(2, company);
            stmt.setString(3, location);
            stmt.setBigDecimal(4, salary);
            stmt.setString(5, description);
            stmt.setInt(6, recruiterId);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                response.sendRedirect("recruiter_dashboard.jsp?success=Job posted successfully!");
            } else {
                response.sendRedirect("recruiter_dashboard.jsp?error=Failed to post job.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("recruiter_dashboard.jsp?error=Database error: " + e.getMessage());
        }
    }
}
