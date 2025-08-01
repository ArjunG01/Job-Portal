package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.arjun.util.DBConnection;

@WebServlet("/ApproveApplicationServlet")
public class ApproveApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String applicationIdStr = request.getParameter("id");
        String status = request.getParameter("status");

        // ✅ Trim inputs to avoid trailing spaces
        applicationIdStr = applicationIdStr != null ? applicationIdStr.trim() : null;
        status = status != null ? status.trim() : null;

        // ✅ Debugging logs
        System.out.println("Application ID Received: " + applicationIdStr);
        System.out.println("Status Received: " + status);

        if (applicationIdStr == null || status == null || 
            (!status.equals("accepted") && !status.equals("rejected"))) {
            response.sendRedirect("recruiter_dashboard.jsp?error=Invalid request parameters.");
            return;
        }

        try {
            int applicationId = Integer.parseInt(applicationIdStr);

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE applications SET status = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, status);
                stmt.setInt(2, applicationId);

                int rowsUpdated = stmt.executeUpdate();
                System.out.println("Rows Updated: " + rowsUpdated);

                if (rowsUpdated > 0) {
                    response.sendRedirect("recruiter_dashboard.jsp?success=Application " + status + " successfully.");
                } else {
                    response.sendRedirect("recruiter_dashboard.jsp?error=No matching application found or already updated.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid application ID format.");
            response.sendRedirect("recruiter_dashboard.jsp?error=Invalid application ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("recruiter_dashboard.jsp?error=Server error, try again later.");
        }
    }
}
