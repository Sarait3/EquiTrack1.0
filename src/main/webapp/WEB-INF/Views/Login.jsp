<%@page import="com.equitrack.service.FormBuilder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Login</title>
	<link rel="stylesheet" href="css/stylesheet.css">
</head>
<body>
	<%
	String errormsg;

	// if the servlet sent a request with the attribute loginFailed, it means
	// that the user attempted to log in, so the error message should display, otherwise it should be null.
	if (request.getAttribute("loginFailed") != null) {
		errormsg = "Login failed, please try again";
	} else {
		errormsg = "";
	}
	
	FormBuilder builder = new FormBuilder();
	
	//build the login form
	builder.setTitle("Login").setAction("Login").setMethod("post").addRequiredInput("text", "Email:", "email")
		.addRequiredInput("password", "Password:", "password").addReset().addError(errormsg);
	%>
	
	<!-- render the login form -->
	<%=builder.createForm(true, false) %>
</body>
</html>