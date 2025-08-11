<%@ page import="com.equitrack.model.Equipment"%>
<%@ page import="com.equitrack.model.User"%>
<%@ page import="com.equitrack.service.PageRoleStrategy"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
Equipment equipment = (Equipment) request.getAttribute("equipment"); // selected equipment
String status = equipment.isOperationalString();                     // "Operational" or "Out Of Service"
User user = (User) request.getAttribute("user");                     // current user

%>
<!DOCTYPE html>
<html>
<head>
<title>Equipment Details</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<%@ include file="Sidebar.jsp"%>

	<div class="header">
		<div class="header-content">
			<%-- Sidebar toggle --%>
			<label for="sidebar-toggle" class="sidebar-button">&#9776;</label>

			<%-- Back to main list --%>
			<a href="ListView" class="back-btn">&larr; Back to List</a>

			<h1>Equipment Details</h1>

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
						alt="<%=equipment.getName()%>" class="equipment-image-detail">
					<div class="equipment-details">
						<div class="equipment-title"><%=equipment.getName()%></div>
						<div class="equipment-id">
							ID:
							<%=equipment.getId()%></div>
						<span class="status-tag status-<%=status.replaceAll(" ", "-")%>"><%=status.toUpperCase()%></span>
					</div>
				</div>
			</div>

			<div class="detail-section">
				<div class="section-title">Equipment Information</div>
				<div class="detail-grid">
					<div class="detail-item">
						<div class="item-label">Location</div>
						<div class="item-value"><%=equipment.getLocation()%></div>
					</div>

					<%-- Show return date only when out of service and available --%>
					<%
					if (!equipment.isOperational() && equipment.getReturnDate() != null) {
					%>
					<div class="detail-item">
						<div class="item-label">Return Date</div>
						<div class="item-value"><%=equipment.getReturnDate()%></div>
					</div>
					<%
					}
					%>
				</div>

				<%-- Optional notes block --%>
				<%
				if (equipment.getNotes() != null && !equipment.getNotes().trim().isEmpty()) {
				%>
				<div class="notes-section">
					<div class="notes-title">NOTES</div>
					<div class="notes-content"><%=equipment.getNotes()%></div>
				</div>
				<%
				}
				%>

				<%-- Actions depend on role strategy and equipment status --%>
				<%
				if (strategy.canCheckout() || ( strategy.canRequestCheckout() && equipment.isOperational())) {
				%>
				<div class='actions-section'>
					<div class='actions-title'>Actions</div>
					<div class='action-buttons'>
						<%-- If item is operational: allow checkout or request checkout --%>
						<%
						if (equipment.isOperational()) {
						%>
						<form method='GET' action='CheckoutForm' style='display: inline-block;'>
							<input type='hidden' name='id' value='<%=equipment.getId()%>'>
							<button type='submit' class='action-btn btn-checkout'>
								<%=strategy.canRequestCheckout() ? "Request Check out" : "Check out"%>
							</button>
						</form>
						<%-- If item is not operational and user can checkout: allow marking back in service --%>
						<%
						} else if (strategy.canCheckout()) {
						%>
						<form method='GET' action='DetailView' style='display: inline-block;'>
							<input type='hidden' name='id' value='<%=equipment.getId()%>'>
							<input type='hidden' name='action' value='backInService'>
							<button type='submit' class='action-btn btn-return'>Back In Service</button>
						</form>
						<%
						}
						%>

						<%-- Edit/Delete available for roles that can checkout (managers/admins) --%>
						<%
						if (strategy.canCheckout()) {
						%>
						<a href='EditEquipment?id=<%=equipment.getId()%>' class='action-btn btn-edit'>Edit Equipment</a>
						<form method='GET' action='DetailView' style='display: inline-block;'>
							<input type='hidden' name='id' value='<%=equipment.getId()%>'>
							<input type='hidden' name='action' value='delete'>
							<button type='submit' class='action-btn btn-delete'>Delete Equipment</button>
						</form>
						<%
						}
						%>
					</div>
				</div>
				<%
				}
				%>

			</div>
		</div>
	</div>

</body>
</html>