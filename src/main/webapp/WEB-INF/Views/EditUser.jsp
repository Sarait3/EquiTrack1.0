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

	form.setTitle("Edit User").setAction("UserManagement")
		.addHiddenInput("action", "doneEditUser").addHiddenInput("id", userToEdit.getId())
		.addRequiredInput("text", "First Name", "fName", userToEdit.getFName())
		.addRequiredInput("text", "Last Name", "lName", userToEdit.getLName());

	if (strategy.getRoleLabel().equalsIgnoreCase("admin")) {
		form.addSelect("Role", "role", new String[][] {
			{ "regular", "Regular", userToEdit.getRole().equalsIgnoreCase("regular") ? "true" : "false" },
			{ "manager", "Manager", userToEdit.getRole().equalsIgnoreCase("manager") ? "true" : "false" },
			{ "admin", "Admin", userToEdit.getRole().equalsIgnoreCase("admin") ? "true" : "false" } });
	} else {
		form.addHiddenInput("role", userToEdit.getRole());
	}

	form.addRequiredInput("text", "Email", "email", userToEdit.getEmail())
		.addRequiredInput("password", "Password", "password", userToEdit.getPassword())
		.addCancel("UserManagement", "Cancel");
	%>
	<%=form.createForm(false, false)%>
</body>
</html>