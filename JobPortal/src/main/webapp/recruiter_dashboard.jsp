<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="com.arjun.util.DBConnection" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%
    // ✅ Disable browser cache to always get fresh data
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
%>

<html>
<head>
    <title>Recruiter Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">

    <%
        HttpSession currentSession = request.getSession(false);
        if (currentSession == null || currentSession.getAttribute("user_id") == null) {
            response.sendRedirect("login.jsp?error=Please login first.");
            return;
        }

        int recruiterId = -1;
        try {
            recruiterId = Integer.parseInt(currentSession.getAttribute("user_id").toString());
        } catch (NumberFormatException e) {
            response.sendRedirect("login.jsp?error=Invalid session.");
            return;
        }
    %>

    <h2>Welcome, <%= currentSession.getAttribute("name") %></h2>

    <h3>Post a New Job</h3>
    <form action="PostJobServlet" method="post" class="mb-4">
        <input type="text" name="title" placeholder="Job Title" required class="form-control mb-2">
        <input type="text" name="company" placeholder="Company Name" required class="form-control mb-2">
        <input type="text" name="location" placeholder="Location" required class="form-control mb-2">
        <input type="number" step="0.01" name="salary" placeholder="Salary (optional)" class="form-control mb-2">
        <textarea name="description" placeholder="Job Description" required class="form-control mb-2"></textarea>
        <button type="submit" class="btn btn-primary">Post Job</button>
    </form>

    <%
        String success = request.getParameter("success");
        String error = request.getParameter("error");
        if (success != null) {
    %>
        <div class="alert alert-success"><%= success %></div>
    <% } else if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <h3>Job Applications</h3>
    <table class="table table-bordered">
        <tr><th>Job Title</th><th>Applicant</th><th>Status</th><th>Action</th></tr>

        <%
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                    "SELECT a.id, j.title, u.name AS applicant_name, a.status " +
                    "FROM applications a " +
                    "JOIN jobs j ON a.job_id = j.id " +
                    "JOIN users u ON a.user_id = u.id " +
                    "WHERE j.posted_by = ?")) {

                stmt.setInt(1, recruiterId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String status = rs.getString("status").trim();
        %>
        <tr>
            <td><%= rs.getString("title") %></td>
            <td><%= rs.getString("applicant_name") %></td>
            <td><%= status %></td>
            <td>
                <% if (!status.equals("accepted")) { %>
                    <a href="ApproveApplicationServlet?id=<%= rs.getInt("id") %>&status=accepted" 
                       class="btn btn-success btn-sm">Approve</a>
                <% } %>
                <% if (!status.equals("rejected")) { %>
                    <a href="ApproveApplicationServlet?id=<%= rs.getInt("id") %>&status=rejected" 
                       class="btn btn-danger btn-sm">Reject</a>
                <% } %>

                <!-- ✅ Delete button for any application -->
                <a href="DeleteApplicationServlet?id=<%= rs.getInt("id") %>" 
                   class="btn btn-warning btn-sm"
                   onclick="return confirm('Are you sure you want to delete this application?');">
                   Delete
                </a>
            </td>
        </tr>
        <%
                }
            } catch (SQLException e) {
                e.printStackTrace();
        %>
            <tr><td colspan="4" class="text-danger">SQL Error: <%= e.getMessage() %></td></tr>
        <%
            } catch (Exception e) {
                e.printStackTrace();
        %>
            <tr><td colspan="4" class="text-danger">Error loading applications.</td></tr>
        <%
            }
        %>
    </table>
</div>
</body>
</html>
