<%@ page import="com.equitrack.model.*" %>

<%-- Data prepared by the servlet --%>
<%
    Equipment equipment = (Equipment) request.getAttribute("equipment"); // equipment being edited
    User user = (User) request.getAttribute("user");                     // current signed-in user
    String formHtml = (String) request.getAttribute("formHtml");         // form markup built by FormBuilder
%>

<%-- Sidebar --%>
<%@ include file="Sidebar.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Edit Equipment</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="header">
    <div class="header-content">
        <%-- Sidebar toggle --%>
        <label for="sidebar-toggle" class="sidebar-button">&#9776;</label>

        <%-- Back to the equipment detail view --%>
        <a href="DetailView?id=<%= equipment.getId() %>" class="back-btn">&larr; Back to Equipment Details</a>

        <h1>Edit Equipment</h1>

        <%-- Current user info and logout --%>
        <div class="user-info">
            <img src="images/user-icon.png" alt="User Icon" class="user-icon">
            <span class="username"><%= user.getFName() %> <%= user.getLName() %></span>
            <a href="Logout" class="back-btn">Logout</a>
        </div>
    </div>
</div>

<div class="container-detail">
    <div class="equipment-detail">

        <%-- Quick equipment summary header --%>
        <div class="detail-header">
            <div class="equipment-info">
                <img src="<%= equipment.getImagePath() %>" alt="<%= equipment.getName() %>" class="equipment-image">
                <div class="equipment-details">
                    <div class="equipment-title"><%= equipment.getName() %></div>
                    <div class="equipment-id">ID: <%= equipment.getId() %></div>
                    <span class="status-tag status-<%= equipment.isOperationalString() %>">
                        <%= equipment.isOperationalString().toUpperCase() %>
                    </span>
                </div>
            </div>
        </div>

        <%-- Render the edit form HTML produced by FormBuilder --%>
        <%= formHtml %>

    </div>
</div>

</body>
</html>
