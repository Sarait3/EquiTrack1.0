<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="com.equitrack.service.PageRoleStrategy"%>
<%@page import="com.equitrack.service.FormBuilder"%>
<%@page import="com.equitrack.model.User"%>
<%@page import="com.equitrack.dao.UserDao"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Edit User</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<%@include file='Sidebar.jsp' %>
	
	<%
	FormBuilder form = new FormBuilder();
	User userToEdit = new UserDao().getUserById(request.getAttribute("id").toString());

	//create the standardized form for editing a user.
	form.setTitle("Edit User").setAction("UserManagement")
		.addHiddenInput("action", "doneEditUser").addHiddenInput("id", userToEdit.getId())
		.addRequiredInput("text", "First Name", "fName", userToEdit.getFName())
		.addRequiredInput("text", "Last Name", "lName", userToEdit.getLName());

	// if the logged in user is an admin, add a selection that allows them to change the
	// role of the user being edited that defaults to that user's current role.
	// otherwise, just add a hidden input containing the role.
	if (strategy.getRoleLabel().equalsIgnoreCase("admin")) {
		form.addSelect("Role", "role", new String[][] {
			{ "regular", "Regular", userToEdit.getRole().equalsIgnoreCase("regular") ? "true" : "false" },
			{ "manager", "Manager", userToEdit.getRole().equalsIgnoreCase("manager") ? "true" : "false" },
			{ "admin", "Admin", userToEdit.getRole().equalsIgnoreCase("admin") ? "true" : "false" } });
	} else {
		form.addHiddenInput("role", userToEdit.getRole());
	}

	//finish the form with the inputs for the user's email and password, and add a cancel button.
	form.addRequiredInput("text", "Email", "email", userToEdit.getEmail())
		.addRequiredInput("password", "Password", "password", userToEdit.getPassword())
		.addCancel("UserManagement", "Cancel");
	%>
	<!-- render the form -->
	<%=form.createForm(false, false)%>
</body>
</html>