<%@ page import="java.util.*" %>
<%@ page import="com.equitrack.model.*" %>

<%
    User user = (User) request.getAttribute("user");
    ArrayList<Request> requestsList = (ArrayList<Request>) request.getAttribute("requestsList");
    String searchInput = (String) request.getAttribute("searchInput");
    String statusFilter = (String) request.getAttribute("statusFilter");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Requests List</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<%@ include file="Sidebar.jsp" %>

<div class="header">
    <div class="header-content">
        <label for="sidebar-toggle" class="sidebar-button">&#9776;</label>
        <h1><a href="ListView" style="color: inherit; text-decoration: none;">Checkout Requests List</a></h1>
        <div class="user-info">
            <img src="images/user-icon.png" alt="User Icon" class="user-icon">
            <span class="username"><%= user.getFName() %> <%= user.getLName() %></span>
            <a href="Logout" class="back-btn">Logout</a>
        </div>
    </div>
</div>

<div class="container">
    <div class="search-bar">
        <form class="search-form" method="GET" action="RequestsList">
            <input type="text" name="searchInput" class="search-input" placeholder="Search requests..." value="<%= searchInput != null ? searchInput : "" %>">

            <select name="statusFilter" class="search-input">
                <option value="" <%= (statusFilter == null || statusFilter.equals("")) ? "selected" : "" %>>All Status</option>
                <option value="pending" <%= "pending".equals(statusFilter) ? "selected" : "" %>>Pending</option>
                <option value="approved" <%= "approved".equals(statusFilter) ? "selected" : "" %>>Approved</option>
                <option value="declined" <%= "declined".equals(statusFilter) ? "selected" : "" %>>Declined</option>
            </select>

            <button type="submit" class="search-btn">Search</button>

            <% if ((searchInput != null && !searchInput.trim().isEmpty()) || (statusFilter != null && !statusFilter.trim().isEmpty())) { %>
                <a href="RequestsList" class="reset-btn">View All</a>
            <% } %>
        </form>
    </div>

    <div class="requests-list">
        <div class="requests-header">
            <div>User Name</div>
            <div>Equipment ID</div>
            <div>Equipment Name</div>
            <div>Location</div>
            <div>Checkout Date</div>
            <div>Return Date</div>
            <div>Status</div>
        </div>

        <div id="requests">
            <% if (requestsList == null || requestsList.isEmpty()) { %>
                <div style="color: #666; text-align: center; font-size: 1.2rem; padding: 1.5rem; margin-top: 2rem; font-style: italic;">
                    No results found.
                </div>
            <% } else {
                for (Request req : requestsList) {
                    User reqUser = (User) request.getAttribute("reqUser_" + req.getId());
                    Equipment eq = (Equipment) request.getAttribute("equipment_" + req.getId());
                    String status = req.getStatus().toLowerCase();
            %>
                <a class="request-item" href="RequestDetail?id=<%= req.getId() %>">
                    <div class="user-name"><%= reqUser.getFName() %> <%= reqUser.getLName() %></div>
                    <div class="equipment-id"><%= eq.getId().substring(0, 8) %></div>
                    <div class="equipment-name"><%= eq.getName() %></div>
                    <div class="equipment-location"><%= req.getLocation() %></div>
                    <div class="checkout-date"><%= req.getCheckoutDate() %></div>
                    <div class="return-date"><%= req.getReturnDate() %></div>
                    <div class="status-cell">
                        <span class="status-tag status-<%= status %>"><%= status.toUpperCase() %></span>
                    </div>
                </a>
            <% }} %>
        </div>
    </div>
</div>

</body>
</html>
