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
		User userToEdit = dao.getUserById(id);
		LoginService login = new LoginService();
		oldPass = login.hashPassword(oldPass);

		if (userToEdit != null && userToEdit.getPassword().equals(oldPass)) {
			userToEdit.setPassword(login.hashPassword(newPass));
			return dao.updateUser(userToEdit);
		}

		return false;
	}

	public boolean changeEmail(String id, String password, String newEmail) {
		UserDao dao = new UserDao();
		User userToEdit = dao.getUserById(id);
		LoginService login = new LoginService();
		password = login.hashPassword(password);

		if (userToEdit != null && userToEdit.getPassword().equals(password)) {
			userToEdit.setEmail(newEmail);
			return dao.updateUser(userToEdit);
		}

		return false;
	}

	public boolean createUser(HttpServletRequest request) {
		String role = request.getParameter("role");
		String fName = request.getParameter("fName");
		String lName = request.getParameter("lName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User newUser = new UserBuilder()
				.setRole(role)
				.setFName(fName)
				.setLName(lName)
				.setEmail(email)
				.setPassword(password) 
				.createUser();
		
		if (newUser == null) {
			System.out.println("[ERROR] UserBuilder returned null. One or more fields were missing.");
			System.out.println("[DEBUG] role=" + role + ", fName=" + fName + ", lName=" + lName + ", email=" + email + ", password=" + password);
			return false;
		}

		return new UserDao().createUser(newUser);
	}

	public boolean deleteUser(String id) {
		return new UserDao().deleteUser(id);
	}

	public String buildEditUser(String id) {
		StringBuilder html = new StringBuilder();
		FormBuilder form = new FormBuilder();
		User userToEdit = dao.getUserById(id);
		
		form.setTitle("Edit User").addHiddenInput("action", "doneEditUser").addHiddenInput("id", userToEdit.getId())
		.addRequiredInput("text", "First Name", "fName", userToEdit.getFName())
		.addRequiredInput("text", "Last Name", "lName", userToEdit.getLName());
		
		if (user.getRole().equalsIgnoreCase("admin")) {
			form.addSelect("Role", "role", new String[][] {
				{"regular", "Regular", userToEdit.getRole().equalsIgnoreCase("regular") ? "true" : "false"}, 
				{"manager", "Manager", userToEdit.getRole().equalsIgnoreCase("manager") ? "true" : "false"}, 
				{"admin", "Admin", userToEdit.getRole().equalsIgnoreCase("admin") ? "true" : "false"}});
		} else {
			form.addHiddenInput("role", userToEdit.getRole());
		}
		
		form.addRequiredInput("text", "Email", "email", userToEdit.getEmail())
		.addRequiredInput("password", "Password", "password", userToEdit.getPassword())
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
