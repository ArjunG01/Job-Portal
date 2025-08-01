<%@ page import="java.sql.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.arjun.util.DBConnection" %>

<html>
<head>
    <title>Job Seeker Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body>
    <div class="container mt-4">
        <h2>Welcome, <%= session.getAttribute("name") %></h2>

        <!-- ✅ Show success or error messages -->
        <% 
            String success = request.getParameter("success");
            String error = request.getParameter("error");
        %>

        <% if (success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>

        <% if (error != null) { %>
            <div class="alert alert-danger"><%= error %></div>
        <% } %>

        <h3>Available Jobs</h3>

        <table class="table table-bordered">
            <tr><th>Title</th><th>Company</th><th>Location</th><th>Salary</th><th>Apply</th></tr>
            <%
                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jobs");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int jobId = rs.getInt("id");
            %>
            <tr>
                <td><%= rs.getString("title") %></td>
                <td><%= rs.getString("company") %></td>
                <td><%= rs.getString("location") %></td>
                <td><%= rs.getDouble("salary") %></td>
                <td>
                    <form action="ApplyJobServlet" method="POST">
                        <input type="hidden" name="job_id" value="<%= jobId %>">
                        <button type="submit" class="btn btn-success">Apply</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</body>
</html>
