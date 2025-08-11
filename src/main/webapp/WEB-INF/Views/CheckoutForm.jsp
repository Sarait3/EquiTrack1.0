<%@ page import="java.util.*"%>
<%@ page import="com.equitrack.model.*"%>
<%
Equipment equipment = (Equipment) request.getAttribute("equipment"); // selected item
Map<UUID, Request> upcomingRequests = (Map<UUID, Request>) request.getAttribute("upcomingRequests"); // approved future bookings
User user = (User) request.getAttribute("user"); // current user
String formHtml = (String) request.getAttribute("formHtml"); // form markup built by FormBuilder
%>

<%@ include file="Sidebar.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Checkout Request</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="css/style.css">
</head>
<body>

	<div class="header">
		<div class="header-content">
			<label for="sidebar-toggle" class="sidebar-button">&#9776;</label> <a
				href="DetailView?id=<%=equipment.getId()%>" class="back-btn">&larr;
				Back to Equipment Details</a>
			<h1>Checkout Request</h1>
			<%-- Current user info and logout --%>
			<div class="user-info">
				<img src="images/user-icon.png" alt="User Icon" class="user-icon">
				<span class="username"><%=user.getFName()%> <%=user.getLName()%></span>
				<a href="Logout" class="back-btn">Logout</a>
			</div>
		</div>
	</div>

	<div class="container-detail">
		<div class="equipment-detail">

			<%-- Equipment summary header --%>
			<div class="detail-header">
				<div class="equipment-info">
					<img src="<%=equipment.getImagePath()%>"
						alt="<%=equipment.getName()%>" class="equipment-image">
					<div class="equipment-details">
						<div class="equipment-title"><%=equipment.getName()%></div>
						<div class="equipment-id">
							ID:
							<%=equipment.getId()%></div>
					</div>
				</div>
			</div>


			<%-- Upcoming approved requests shown as unavailable date ranges --%>
			<%
			if (!upcomingRequests.isEmpty()) {
			%>
			<div class="unavailable-box">
				<div class="unavailable-title">Unavailable Dates</div>
				<ul class="unavailable-list">
					<%
					for (Request r : upcomingRequests.values()) {
					%>
					<li><%=r.getCheckoutDate()%> to <%=r.getReturnDate()%></li>
					<%
					}
					%>
				</ul>
			</div>
			<%
			}
			%>

			<%-- Render the checkout form --%>
			<%=formHtml%>

		</div>
	</div>

</body>
</html>
