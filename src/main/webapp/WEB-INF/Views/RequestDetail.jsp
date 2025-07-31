<%@ page import="com.equitrack.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) request.getAttribute("user");
    Request req = (Request) request.getAttribute("request");
    Equipment equipment = (Equipment) request.getAttribute("equipment");
    User reqUser = (User) request.getAttribute("reqUser");
    String status = req.getStatus();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Request Details</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<%@ include file="Sidebar.jsp" %>
<div class="header">
    <div class="header-content">
        <label for="sidebar-toggle" class="sidebar-button">&#9776;</label>
        <a href="RequestsList" class="back-btn">&larr; Back to List</a>
        <h1>Request Details</h1>
        <div class="user-info">
            <img src="images/user-icon.png" alt="User Icon" class="user-icon">
            <span class="username"><%= user.getFName() %> <%= user.getLName() %></span>
            <a href="Logout" class="back-btn">Logout</a>
        </div>
    </div>
</div>
<div class="container-detail">
    <div class="equipment-detail">
        <div class="detail-header">
            <div class="equipment-info">
                <img src="<%= equipment.getImagePath() %>" alt="<%= equipment.getName() %>" class="equipment-image-detail">
                <div class="equipment-details">
                    <div class="equipment-title">Request for: <%= equipment.getName() %></div>
                    <div class="equipment-id">Request ID: <%= req.getId().substring(0, 8) %></div>
                    <div class="equipment-id">Request Date: <%= req.getRequestDate() %></div>
                    <span class="status-tag status-<%= status.toLowerCase() %>"><%= status.toUpperCase() %></span>
                </div>
            </div>
        </div>
        <div class="detail-section">
            <div class="section-title">Request Information</div>
            <div class="detail-grid">
                <div class="detail-item">
                    <div class="item-label">User</div>
                    <div class="item-value"><%= reqUser.getFName() %> <%= reqUser.getLName() %></div>
                </div>
                <div class="detail-item">
                    <div class="item-label">Location</div>
                    <div class="item-value"><%= equipment.getLocation() %></div>
                </div>
                <div class="detail-item">
                    <div class="item-label">Checkout Date</div>
                    <div class="item-value"><%= req.getCheckoutDate() %></div>
                </div>
                <div class="detail-item">
                    <div class="item-label">Return Date</div>
                    <div class="item-value"><%= req.getReturnDate() %></div>
                </div>
            </div>
            <% if (req.getNotes() != null && !req.getNotes().trim().isEmpty()) { %>
                <div class="notes-section">
                    <div class="notes-title">NOTES</div>
                    <div class="notes-content"><%= req.getNotes() %></div>
                </div>
            <% } %>
            <% if (strategy.canApproveOrDeclineRequests()) { %>
                <div class="form-buttons" style="text-align:center;">
                    <form method="GET" action="RequestDetail" style="display:inline-block;">
                        <input type="hidden" name="id" value="<%= req.getId() %>">
                        <input type="hidden" name="action" value="approve">
                        <button type="submit" class="action-btn btn-return">Approve</button>
                    </form>
                    <form method="GET" action="RequestDetail" style="display:inline-block;">
                        <input type="hidden" name="id" value="<%= req.getId() %>">
                        <input type="hidden" name="action" value="decline">
                        <button type="submit" class="action-btn btn-delete">Decline</button>
                    </form>
                </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
