<%@ page import="com.equitrack.model.User"%>
<%@ page import="com.equitrack.model.Equipment"%>
<%@ page import="com.equitrack.service.PageRoleStrategy"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
    User user = (User) request.getAttribute("user");
    java.util.ArrayList<Equipment> equipmentList = (java.util.ArrayList<Equipment>) request.getAttribute("equipmentList");
    String searchInput = (String) request.getAttribute("searchInput");
    String statusFilter = (String) request.getAttribute("statusFilter");
%>

<%@ include file="Sidebar.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Equipment List</title>
    <link rel='stylesheet' href='css/style.css'>
</head>
<body>

<div class='header'>
    <div class='header-content'>
        <label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>
        <h1><a href='ListView' style='color: inherit; text-decoration: none;'>EquiTrack List</a></h1>
        <div class='user-info'>
            <img src='images/user-icon.png' alt='User Icon' class='user-icon'>
            <span class='username'><%= user.getFName() %> <%= user.getLName() %></span>
            <a href='Logout' class='back-btn'>Logout</a>
        </div>
    </div>
</div>

<div class='container'>

    <div class='search-bar'>
        <form class='search-form' method='GET' action='ListView'>
            <input type='text' name='searchInput' class='search-input' placeholder='Search equipment...' value='<%= searchInput != null ? searchInput : "" %>'>

            <select name='statusFilter' class='search-input'>
                <option value='' <%= (statusFilter == null || "".equals(statusFilter)) ? "selected" : "" %>>All Status</option>
                <option value='operational' <%= "operational".equalsIgnoreCase(statusFilter) ? "selected" : "" %>>Operational</option>
                <option value='out of service' <%= "out of service".equalsIgnoreCase(statusFilter) ? "selected" : "" %>>Out Of Service</option>
            </select>

            <button type='submit' class='search-btn'>Search</button>

            <%
                boolean hasSearch = searchInput != null && !searchInput.trim().isEmpty();
                boolean hasStatus = statusFilter != null && !statusFilter.trim().isEmpty();
                if (hasSearch || hasStatus) {
            %>
                <a href='ListView' class='reset-btn'>View All</a>
            <%
                } else if (strategy.canAddEquipment()) {
            %>
                <a href='AddEquipment' class='add-btn'>Add Equipment</a>
            <%
                }
            %>
        </form>
    </div>

    <div class='equipment-list'>
        <div class='equipment-header'>
            <div>Image</div>
            <div>ID</div>
            <div>Name</div>
            <div>Location</div>
            <div>Status</div>
        </div>
        <div id='equipmentItems'>
            <%
                if (equipmentList == null || equipmentList.isEmpty()) {
            %>
                <div style='color: #666; text-align: center; font-size: 1.2rem; padding: 1.5rem; margin-top: 2rem; font-style: italic;'>No results found.</div>
            <%
                } else {
                    for (Equipment eq : equipmentList) {
                        String status = eq.isOperationalString();
            %>
                <a class='equipment-item' href='DetailView?id=<%= eq.getId() %>'>
                    <img class='equipment-image' src='<%= eq.getImagePath() %>' alt='<%= eq.getName() %>'>
                    <div class='equipment-id'><%= eq.getId().substring(0, 8) %></div>
                    <div class='equipment-name'><%= eq.getName() %></div>
                    <div class='equipment-location'><%= eq.getLocation() %></div>
                    <span class='status-tag status-<%= status.replaceAll(" ", "-") %>'><%= status.toUpperCase() %></span>
                </a>
            <%
                    }
                }
            %>
        </div>
    </div>

</div>

</body>
</html>
