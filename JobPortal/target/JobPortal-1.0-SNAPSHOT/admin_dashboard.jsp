<%@ page import="java.sql.*" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.arjun.util.DBConnection" %>

<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Admin Dashboard</h2>

        <h3>Manage Users</h3>
        <table class="table table-bordered">
            <tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Action</th></tr>
            <%
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE role != 'admin'");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("full_name") %></td>
                <td><%= rs.getString("email") %></td>
                <td><%= rs.getString("role") %></td>
                <td><a href="DeleteUserServlet?id=<%= rs.getInt("id") %>" class="btn btn-danger">Delete</a></td>
            </tr>
            <% } %>
        </table>
    </div>
</body>
</html>
