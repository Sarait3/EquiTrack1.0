package com.equitrack.service;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class UserManagementService extends PageBuilder {
	protected User user;
	protected UserDao dao;
	private PageRoleStrategy strategy;

	public UserManagementService(User user) {
		this.user = user;
		this.dao = new UserDao();

		switch (user.getRole().toLowerCase()) {
		case "admin":
			strategy = new AdminPageStrategy();
			break;
		case "manager":
			strategy = new ManagerPageStrategy();
			break;
		default:
			strategy = new RegularUserPageStrategy();
			break;

		}
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();

		html.append("<!DOCTYPE html><html lang='en'><head>")
		.append("<meta charset='UTF-8'>")
		.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
		.append("<title>User Management</title>")
		.append("<link rel='stylesheet' href='css/style.css'>")
		.append("<style>table {margin-left:auto; margin-right:auto;}</style>")
		.append("</head><body>")
		.append(strategy.buildSidebar())
		.append("<div class='header'><div class='header-content'>")
		.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
		.append("<h1><a href='ListView' style='color: inherit; text-decoration: none;'>User Management</a></h1>")
		.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
		.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
		.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>")
		.append(strategy.buildUserList())
		.append(strategy.buildCreateUser())
		.append(strategy.buildManageAccount());

		html.append("</body></html>");

		return html.toString();
	}

}
