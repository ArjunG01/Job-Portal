package arjun;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.arjun.util.DBConnection;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null) {
            try (Connection conn = DBConnection.getConnection()) {
                int userId = Integer.parseInt(idStr);
                String sql = "DELETE FROM users WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                int rows = stmt.executeUpdate();

                if (rows > 0) {
                    response.sendRedirect("admin_dashboard.jsp?success=User deleted successfully.");
                } else {
                    response.sendRedirect("admin_dashboard.jsp?error=User not found.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("admin_dashboard.jsp?error=Server error.");
            }
        } else {
            response.sendRedirect("admin_dashboard.jsp?error=Invalid request.");
        }
    }
}
