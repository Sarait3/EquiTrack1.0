<%@ page import="com.equitrack.model.User" %>
<%@ page import="com.equitrack.service.PageRoleStrategy" %>

<%
    PageRoleStrategy strategy = (PageRoleStrategy) request.getAttribute("sidebarStrategy");
%>

<input type="checkbox" id="sidebar-toggle" hidden>
<div class="sidebar">
    <nav>
        <ul>
            <li><a href="ListView">Equipment List</a></li>

            <% if (strategy.canViewAllRequests()) { %>
                <li><a href="RequestsList">Checkout Requests</a></li>
            <% } else { %>
                <li><a href="RequestsList">My Checkout Requests</a></li>
            <% } %>

            <li><a href="UserManagement">User Management</a></li>
        </ul>
    </nav>
</div>
