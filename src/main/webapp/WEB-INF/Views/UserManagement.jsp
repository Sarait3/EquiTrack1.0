<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.equitrack.service.FormBuilder"%>
<%@page import="com.equitrack.dao.UserDao"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.UUID"%>

<%
User user = (User) request.getAttribute("user");
StringBuilder html = new StringBuilder();
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>User Management</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="css/style.css">
	<style>
		table { margin: auto}
	</style>
</head>
<body>
	<%@ include file="Sidebar.jsp"%>

	<div class="header">
		<div class="header-content">
			<label for="sidebar-toggle" class="sidebar-button">&#9776;</label> <a href="ListView" class="back-btn">&larr; Back to List View</a>
			<h1>User Management</h1>
			<div class="user-info">
				<img src="images/user-icon.png" alt="User Icon" class="user-icon">
				<span class="username"><%=user.getFName()%> <%=user.getLName()%></span>
				<a href="Logout" class="back-btn">Logout</a>
			</div>
		</div>
	</div>

	<%
	UserDao dao = new UserDao();
	if (strategy.canManageUsers()) {
		Map<UUID, User> users = dao.getAllUsers();
		%>
		<div style="text-align: center" class="action-section container-detail">
			<h1>Manage Users</h1>
			<table>
				<tr>
					<th>Name</th>
					<th>Actions</th>
				</tr>
				<%
				for (User listUser : users.values()) {
				%>
				<tr>
					<td><%=listUser.getLName()%>, <%=listUser.getFName()%></td>
					<td><a href="UserManagement?action=edituser&id=<%=listUser.getId()%>">Edit User</a></td>
					<%
					if (!listUser.getRole().equalsIgnoreCase("admin") && strategy.canDeleteUsers()) {
					%>
					<td><a href="UserManagement?action=deleteuser&id=<%=listUser.getId()%>">Delete User</a></td>
					<%
					}
				}
					%>
				</tr>
			</table>
		</div>
		<%
	}
	%>

	<% 
	if (strategy.canCreateUsers()) {
		FormBuilder createUserForm = new FormBuilder();
		
		createUserForm.setTitle("Create User").addHiddenInput("action", "createUser")
				.addRequiredInput("text", "First Name", "fName").addRequiredInput("text", "Last Name", "lName")
				.addSelect("Role", "role",
				new String[][] { { "regular", "Regular" }, { "manager", "Manager" }, { "admin", "Admin" } })
				.addRequiredInput("text", "Email", "email").addRequiredInput("password", "Password", "password")
				.addRequiredInput("password", "Repeat Password", "repeatedpass");
		%>
	
		<div class='action-section'>
			<%=createUserForm.createForm(false, false) %>
	</div>
	<%} %>

	<%
	FormBuilder changePass = new FormBuilder();
	FormBuilder changeEmail = new FormBuilder();

	changePass.setTitle("Change Password").addHiddenInput("action", "changepassword")
			.addRequiredInput("password", "Old Password", "oldPass")
			.addRequiredInput("password", "New Password", "newPass");

	changeEmail.setTitle("Change Email").addHiddenInput("action", "changeemail")
			.addRequiredInput("text", "New Email", "newmail").addRequiredInput("password", "Password", "password");
	%>

	<div class='action-section'>
		<%=changePass.createForm(false, false)%>
		<%=changeEmail.createForm(false, false)%>
	</div>;
</body>
</html>
