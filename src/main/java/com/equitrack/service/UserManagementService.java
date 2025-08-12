package com.equitrack.service;

import javax.servlet.http.HttpServletRequest;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class UserManagementService {
	private UserDao dao;

	/**
	 * Initializes a UserManagementService object and generates the initial HTML for
	 * the User Management page based on the role of the provided User object.
	 * 
	 * @param user The currently logged in User to display the page to
	 */
	public UserManagementService(User user) {
		this.dao = new UserDao();
	}

	/**
	 * Changes the password of the user with the provided id.
	 * 
	 * @param id      The UUID (in String format) of the user to be updated
	 * @param oldPass The user's old password
	 * @param newPass The user's new password
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean changePassword(String id, String oldPass, String newPass) {
		UserDao dao = new UserDao();
		User userToEdit = dao.getUserById(id);
		LoginService login = new LoginService();
		oldPass = login.hashPassword(oldPass);

		if (userToEdit != null && userToEdit.getPassword().equals(oldPass)) {
			userToEdit.setPassword(newPass);
			return dao.updateUser(userToEdit);
		}

		return false;
	}

	/**
	 * Changes the email address of the user with the provided id
	 * 
	 * @param id       The UUID (in String format) of the user to be updated
	 * @param password The user's password
	 * @param newEmail the user's new email address
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean changeEmail(String id, String password, String newEmail) {
		UserDao dao = new UserDao();
		User userToEdit = dao.getUserById(id);
		LoginService login = new LoginService();
		String hashpassword = login.hashPassword(password);

		if (userToEdit != null && userToEdit.getPassword().equals(hashpassword)) {
			userToEdit.setEmail(newEmail);
			userToEdit.setPassword(password);
			return dao.updateUser(userToEdit);
		}

		return false;
	}

	/**
	 * Processes information entered in the create user form found in the
	 * AdminPageStrategy class.
	 * 
	 * @param request The HTML post request containing the parameters of the user to
	 *                be created
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean createUser(HttpServletRequest request) {
		String role = request.getParameter("role");
		String fName = request.getParameter("fName");
		String lName = request.getParameter("lName");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User newUser = new UserBuilder().setRole(role).setFName(fName).setLName(lName).setEmail(email)
				.setPassword(password).createUser();

		return new UserDao().createUser(newUser);
	}

	/**
	 * Calls the UserDao class' deleteUser() method to remove the desired user from
	 * the 'users' table in the database
	 * 
	 * @param id The UUID (in String format) of the user to be deleted
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean deleteUser(String id) {
		return new UserDao().deleteUser(id);
	}

	public boolean editUser(User user) {
		return dao.updateUser(user);
	}
}
