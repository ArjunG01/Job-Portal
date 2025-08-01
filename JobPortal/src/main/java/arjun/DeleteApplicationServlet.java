package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.arjun.util.DBConnection;

@WebServlet("/DeleteApplicationServlet")
public class DeleteApplicationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appIdStr = request.getParameter("id");

        if (appIdStr != null) {
            try (Connection conn = DBConnection.getConnection()) {
                int appId = Integer.parseInt(appIdStr);

                String sql = "DELETE FROM applications WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, appId);

                int deleted = stmt.executeUpdate();
                if (deleted > 0) {
                    response.sendRedirect("recruiter_dashboard.jsp?success=Application deleted successfully.");
                } else {
                    response.sendRedirect("recruiter_dashboard.jsp?error=Failed to delete.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("recruiter_dashboard.jsp?error=Server error.");
            }
        } else {
            response.sendRedirect("recruiter_dashboard.jsp?error=Invalid request.");
        }
    }
}
