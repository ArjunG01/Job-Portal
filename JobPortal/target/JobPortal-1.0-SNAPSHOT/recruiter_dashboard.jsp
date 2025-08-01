<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.arjun.util.DBConnection" %>

<html>
<head>
    <title>Recruiter Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Welcome, <%= session.getAttribute("full_name") %></h2>
        <h3>Post a New Job</h3>
        <form action="PostJobServlet" method="post">
            <input type="text" name="title" placeholder="Job Title" required>
            <input type="text" name="company" placeholder="Company Name" required>
            <input type="text" name="location" placeholder="Location" required>
            <input type="number" name="salary" placeholder="Salary" required>
            <button type="submit" class="btn btn-primary">Post Job</button>
        </form>

        <h3>Job Applications</h3>
        <table class="table table-bordered">
            <tr><th>Job Title</th><th>Applicant</th><th>Status</th><th>Action</th></tr>
            <%
                int recruiterId = (int) session.getAttribute("user_id");
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT a.id, j.title, u.full_name, a.status FROM applications a JOIN jobs j ON a.job_id = j.id JOIN users u ON a.user_id = u.id WHERE j.posted_by=?");
                stmt.setInt(1, recruiterId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
            %>
            <tr>
                <td><%= rs.getString("title") %></td>
                <td><%= rs.getString("full_name") %></td>
                <td><%= rs.getString("status") %></td>
                <td>
                    <a href="ApproveApplicationServlet?id=<%= rs.getInt("id") %>&status=Approved" class="btn btn-success">Approve</a>
                    <a href="ApproveApplicationServlet?id=<%= rs.getInt("id") %>&status=Rejected" class="btn btn-danger">Reject</a>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</body>
</html>
