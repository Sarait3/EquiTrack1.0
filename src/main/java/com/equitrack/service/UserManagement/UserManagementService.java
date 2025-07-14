package com.equitrack.service.UserManagement;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;
import com.equitrack.service.PageBuilder;

public class UserManagementService extends PageBuilder {
	protected User user;
	protected UserDao dao;
	
	public UserManagementService(User user) {
		this.user = user;
		this.dao = new UserDao();
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		
		html.append("<!DOCTYPE html><html lang='en'><head>")
		.append("<meta charset='UTF-8'>")
		.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
		.append("<title>Checkout Request</title>")
		.append("<link rel='stylesheet' href='css/style.css'>")
		.append("<style>table {margin-left:auto; margin-right:auto;}</style>")
		.append("</head><body>");

	html.append("<input type='checkbox' id='sidebar-toggle' hidden>")
		.append("<div class='sidebar'><nav><ul>")
		.append("<li><a href='ListView'>Equipment List</a></li>");
	
	switch (user.getRole().toLowerCase()) {
	case "regular":
		html.append(new RegularUserManagementService(user).buildPage());
		break;
	case "admin":
		html.append(new AdminUserManagementService(user).buildPage());
		break;
	case "manager":
		html.append(new ManagerUserManagementService(user).buildPage());
		break;
	}
	
	html.append("</body></html>");
		
		return html.toString();
	}

}
