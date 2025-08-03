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
table {
	margin: auto
}
</style>
</head>
<body>
	<%@ include file="Sidebar.jsp"%>

	<div class="header">
		<div class="header-content">
			<label for="sidebar-toggle" class="sidebar-button">&#9776;</label> <a
				href="ListView" class="back-btn">&larr; Back to List View</a>
			<h1>User Management</h1>
			<div class="user-info">
				<img src="images/user-icon.png" alt="User Icon" class="user-icon">
				<span class="username"><%=user.getFName()%> <%=user.getLName()%></span>
				<a href="Logout" class="back-btn">Logout</a>
			</div>
		</div>
	</div>

	<%
	html = new StringBuilder();

	if (strategy.canManageUsers()) {
		UserDao dao = new UserDao();
		Map<UUID, User> users = dao.getAllUsers();

		html.append("<div style='text-align:center' class='action-section container-detail'>")
		.append("<h1>Manage Users</h1>").append("<table>").append("<th>Name</th><th>Actions</th>");

		for (User listUser : users.values()) {
			if (listUser.getRole().equalsIgnoreCase("regular")) {
		html.append("<tr>").append(String.format("<td>%s, %s</td>", listUser.getLName(), listUser.getFName()))
				.append(String.format("<td><a href='UserManagement?action=edituser&id=%s'>Edit User</a></td>",
						listUser.getId()));
		if (strategy.canDeleteUsers()) {
			html.append(String.format("<td><a href='UserManagement?action=deleteuser&id=%s'>Delete User</a></td>",
					listUser.getId()));
		}
		html.append("</tr>");
			}
		}
		html.append("</table></div>");
	}

	if (strategy.canCreateUsers()) {
		FormBuilder createUserForm = new FormBuilder();

		createUserForm.setTitle("Create User").addHiddenInput("action", "createUser")
		.addRequiredInput("text", "First Name", "fName").addRequiredInput("text", "Last Name", "lName")
		.addSelect("Role", "role",
				new String[][] { { "regular", "Regular" }, { "manager", "Manager" }, { "admin", "Admin" } })
		.addRequiredInput("text", "Email", "email").addRequiredInput("password", "Password", "password")
		.addRequiredInput("password", "Repeat Password", "repeatedpass");

		html.append("<div class='action-section'>" + createUserForm.createForm(false, false) + "</div>");
	}

	FormBuilder changePass = new FormBuilder();
	FormBuilder changeEmail = new FormBuilder();

	changePass.setTitle("Change Password").addHiddenInput("action", "changepassword")
			.addRequiredInput("password", "Old Password", "oldPass")
			.addRequiredInput("password", "New Password", "newPass");

	changeEmail.setTitle("Change Email").addHiddenInput("action", "changeemail")
			.addRequiredInput("text", "New Email", "newmail").addRequiredInput("password", "Password", "password");

	html.append("<div class='action-section'>").append(changePass.createForm(false, false))
			.append(changeEmail.createForm(false, false)).append("</div>");
	%>
	<%=html.toString()%>


</body>
</html>