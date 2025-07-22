package com.equitrack.service;

import javax.servlet.http.HttpServletRequest;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class UserManagementService extends PageBuilder {
	private User user;
	private UserDao dao;
	private PageRoleStrategy strategy;
	private String page;

	/**
	 * Initializes a UserManagementService object and generates the initial HTML 
	 * for the User Management page based on the role of the provided User object.
	 * @param user	The currently logged in User to display the page to
	 */
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

	/**
	 * Constructs the rest of the Page that wasn't done in the constructor
	 * based on the User's role.
	 */
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

	/**
	 * Changes the password of the user with the provided id.
	 * @param id		The UUID (in String format) of the user to be updated
	 * @param oldPass	The user's old password
	 * @param newPass	The user's new password
	 * @return			Returns true if the operation was successful, false otherwise
	 */
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

	/**
	 * Changes the email address of the user with the provided id
	 * @param id		The UUID (in String format) of the user to be updated
	 * @param password	The user's password
	 * @param newEmail	the user's new email address
	 * @return			Returns true if the operation was successful, false otherwise
	 */
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

	/**
	 * Processes information entered in the create user form found in the
	 * AdminPageStrategy class.
	 * @param request	The HTML post request containing the parameters of the user to be created
	 * @return	Returns true if the operation was successful, false otherwise
	 */
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

		return new UserDao().createUser(newUser);
	}

	/**
	 *  Calls the UserDao class' deleteUser() method to remove the desired user from 
	 *  the 'users' table in the database
	 * @param id	The UUID (in String format) of the user to be deleted
	 * @return		Returns true if the operation was successful, false otherwise
	 */
	public boolean deleteUser(String id) {
		return new UserDao().deleteUser(id);
	}

	/**
	 * Generates the HTML for an edit user page in which an admin or manager can 
	 * Edit the details of a user.
	 * @param id	The UUID (in String format) of the user to be edited
	 * @return		Returns true if the operation was successful, false otherwise
	 */
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
