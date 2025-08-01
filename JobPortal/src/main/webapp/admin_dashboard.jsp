<%@ page import="java.sql.*" %>
<%@ page import="com.arjun.util.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    // ✅ Ensure only logged-in admins can access
    HttpSession sessionUser = request.getSession(false);
    if (sessionUser == null || sessionUser.getAttribute("role") == null || !"admin".equals(sessionUser.getAttribute("role"))) {
        response.sendRedirect("login.jsp?error=Unauthorized Access");
        return;
    }
%>

<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Admin Dashboard</h2>

        <%-- ✅ Display success/error message if available --%>
        <%
            String success = request.getParameter("success");
            String error = request.getParameter("error");

            if (success != null) {
        %>
            <div class="alert alert-success"><%= success %></div>
        <% } else if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <h3>Manage Users</h3>
        <table class="table table-bordered">
            <tr>
                <th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Action</th>
            </tr>
            <%
                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;
                try {
                    conn = DBConnection.getConnection();
                    if (conn == null) {
                        throw new Exception("Database connection failed!");
                    }

                    stmt = conn.prepareStatement("SELECT id, name, email, role FROM users WHERE role != 'admin'");
                    rs = stmt.executeQuery();
                    
                    while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("name") %></td>
                <td><%= rs.getString("email") %></td>
                <td><%= rs.getString("role") %></td>
                <td>
                    <a href="DeleteUserServlet?id=<%= rs.getInt("id") %>" 
                       class="btn btn-danger" 
                       onclick="return confirm('Are you sure you want to delete this user?');">
                        Delete
                    </a>
                </td>
            </tr>
            <% 
                    }
                } catch (Exception e) {
                    out.println("<tr><td colspan='5' class='text-danger'>Error fetching users: " + e.getMessage() + "</td></tr>");
                    e.printStackTrace();
                } finally {
                    if (rs != null) try { rs.close(); } catch (Exception ignored) {}
                    if (stmt != null) try { stmt.close(); } catch (Exception ignored) {}
                    if (conn != null) try { conn.close(); } catch (Exception ignored) {}
                }
            %>
        </table>
    </div>
</body>
</html>
