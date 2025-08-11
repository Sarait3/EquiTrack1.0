<%@ page import="com.equitrack.service.PageRoleStrategy" %>

<%-- Sidebar menu; expects "sidebarStrategy" set by the servlet --%>
<%
    PageRoleStrategy strategy = (PageRoleStrategy) request.getAttribute("sidebarStrategy");
%>

<input type="checkbox" id="sidebar-toggle" hidden>
<div class="sidebar">
    <nav>
        <ul>
            <li><a href="ListView">Equipment List</a></li>

            <%-- Managers/Admins see all requests; others see only their own --%>
            <% if (strategy != null && strategy.canViewAllRequests()) { %>
                <li><a href="RequestsList">Checkout Requests</a></li>
            <% } else { %>
                <li><a href="RequestsList">My Checkout Requests</a></li>
            <% } %>

            <%-- Link is visible to all --%>
            <li><a href="UserManagement">User Management</a></li>
        </ul>
    </nav>
</div>
