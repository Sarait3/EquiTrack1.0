package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.model.User;
import com.equitrack.service.ConfirmationPageBuilder;
import com.equitrack.service.UserManagementService;

/**
 * Servlet implementation class UserManagement
 */
@WebServlet("/UserManagement")
public class UserManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserManagementServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");

		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		String action = request.getParameter("action");

		if (action != null) {
			switch (action) {
			case "changepassword":
				break;
			case "changeemail":
				break;
			case "deleteuser":
				break;
			default:
				response.getWriter().write(new UserManagementService(user).buildPage());
			}
		} else {
			response.getWriter().write(new UserManagementService(user).buildPage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		UserManagementService userManagement = new UserManagementService(user);
		User userToEdit;

		switch (request.getParameter("action")) {
		case "createUser":
			if (userManagement.createUser(request)) {
				response.getWriter().write(
						new ConfirmationPageBuilder("User Created Sucessfully", "UserManagement", true).buildPage());
			} else
				response.getWriter().write(
						new ConfirmationPageBuilder("User Creation Failed", "UserManagement", false).buildPage());
			break;
		case "changepassword":
			if (request.getParameter("password").equals(request.getParameter("repeatedpass"))) {
				if (userManagement.changePassword(user.getId(), request.getParameter("password"),
						request.getParameter("repeatedpass"))) {
					response.getWriter()
							.write(new ConfirmationPageBuilder("Password Changed Sucessfully", "UserManagement", true)
									.buildPage());
				}
			} else
				response.getWriter().write(
						new ConfirmationPageBuilder("Password Change Failed", "UserManagement", false).buildPage());
			break;
		case "deleteuser":
			if (userManagement.deleteUser(request.getParameter("id"))) {
				response.getWriter().write(
						new ConfirmationPageBuilder("User Deleted Sucessfully", "UserManagement", true).buildPage());
			} else
				response.getWriter().write(
						new ConfirmationPageBuilder("User Deletion Failed", "UserManagement", false).buildPage());
			break;
		case "edituser":
			response.getWriter().write(new UserManagementService(user).buildEditUser(request.getParameter("id")));
			break;
		case "doneEditUser":
			userToEdit = new User(request.getParameter("id"), request.getParameter("role"),
					request.getParameter("fName"), request.getParameter("lName"), request.getParameter("email"),
					request.getParameter("password"));
			if (userManagement.editUser(userToEdit)) {
				response.getWriter().write(
						new ConfirmationPageBuilder("User Edited Sucessfully", "UserManagement", true).buildPage());
			} else
				response.getWriter()
						.write(new ConfirmationPageBuilder("User Editing Failed", "UserManagement", false).buildPage());
			break;
		case "changeemail":
			if (userManagement.changePassword(user.getId(), request.getParameter("password"),
					request.getParameter("repeatedpass"))) {
				response.getWriter()
						.write(new ConfirmationPageBuilder("Password Changed Sucessfully", "UserManagement", true)
								.buildPage());
			} else
				response.getWriter().write(
						new ConfirmationPageBuilder("Password Change Failed", "UserManagement", false).buildPage());
			break;
		default:
			response.getWriter().write(new UserManagementService(user).buildPage());
		}
	}
}
