package com.equitrack.service;

import javax.servlet.http.HttpServletRequest;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class UserManagementService extends PageBuilder {
	private User user;
	private UserDao dao;
	private PageRoleStrategy strategy;
	private String page;

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
		
		page = new StringBuilder().append("<!DOCTYPE html><html lang='en'><head>")
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
		.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>").toString();
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		
		html.append(page)
		.append(strategy.buildUserList())
		.append(strategy.buildCreateUser())
		.append(strategy.buildManageAccount())
		.append("</body></html>");
		return html.toString();
	}

	public boolean changePassword(String id, String oldPass, String newPass) {
		UserDao dao = new UserDao();
		User user = dao.getUserById(id);
		LoginService login = new LoginService();
		oldPass = login.hashPassword(oldPass);

		if (user != null && user.getPassword().equals(oldPass)) {
			user.setPassword(newPass);
			return dao.updateUser(user);
		}

		return false;
	}

	public boolean changeEmail(String id, String password, String newEmail) {
		UserDao dao = new UserDao();
		User user = dao.getUserById(id);
		LoginService login = new LoginService();
		password = login.hashPassword(password);

		if (user != null && user.getPassword().equals(password)) {
			user.setEmail(newEmail);
			return dao.updateUser(user);
		}

		return false;
	}

	public boolean createUser(HttpServletRequest request) {
		String role = request.getParameter("role");
		String fName = request.getParameter("fName");
		String lName = request.getParameter("lName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User newUser = new User(role, fName, lName, email, password);
		return new UserDao().createUser(newUser);
	}

	public boolean deleteUser(String id) {
		return new UserDao().deleteUser(id);
	}

	public String buildEditUser(String id) {
		StringBuilder html = new StringBuilder();
		FormBuilder form = new FormBuilder();
		User editUser = dao.getUserById(id);
		
		form.setTitle("Edit User").addHiddenInput("action", "doneEditUser").addHiddenInput("id", editUser.getId())
		.addRequiredInput("text", "First Name", "fName", editUser.getFName())
		.addRequiredInput("text", "Last Name", "lName", editUser.getLName())
		.addSelect("Role", "role", new String[][] {
			{"regular", "Regular", editUser.getRole().equalsIgnoreCase("regular") ? "true" : "false"}, 
			{"manager", "Manager", editUser.getRole().equalsIgnoreCase("manager") ? "true" : "false"}, 
			{"admin", "Admin", editUser.getRole().equalsIgnoreCase("admin") ? "true" : "false"}})
		.addRequiredInput("text", "Email", "email", editUser.getEmail())
		.addRequiredInput("password", "Password", "password", editUser.getPassword())
		.addCancel("UserManagement", "Cancel");

		html.append(page)
			.append(form.createForm(false, false))
			.append("</body></html>");
		
		return html.toString();
	}

	public boolean editUser(User user) {
		return dao.updateUser(user);
	}
}
