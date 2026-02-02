package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.model.User;
import com.equitrack.service.AdminPageStrategy;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.PageRoleStrategy;
import com.equitrack.service.RegularUserPageStrategy;
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
		UserManagementService userManagement = new UserManagementService(user);
		String action = request.getParameter("action");
		PageRoleStrategy strategy = null;

		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		if (user != null) {
			switch (user.getRole().toLowerCase()) {
			case "manager":
				strategy = new ManagerPageStrategy();
				break;
			case "admin":
				strategy = new AdminPageStrategy();
				break;
			default:
				strategy = new RegularUserPageStrategy();
				break;
			}

			request.setAttribute("strategy", strategy);
			request.setAttribute("sidebarStrategy", strategy);
		}

		if (action != null) {
			switch (action) {
			case "edituser":
				request.setAttribute("id", request.getParameter("id"));
				request.getRequestDispatcher("/WEB-INF/Views/EditUser.jsp").forward(request, response);
				break;
			case "deleteuser":
				if (userManagement.deleteUser(request.getParameter("id"))) {
					request.setAttribute("message", "User Deleted Successfully");
					request.setAttribute("returnTo", "UserManagement");
					request.setAttribute("isSuccessful", true);
					request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
				} else {
					request.setAttribute("message", "User Deletion Successfully");
					request.setAttribute("returnTo", "UserManagement");
					request.setAttribute("isSuccessful", true);
					request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
				}
				break;
			default:
				request.setAttribute("user", user);
				request.getRequestDispatcher("/WEB-INF/Views/UserManagement.jsp").forward(request, response);
			}
		} else {
			request.setAttribute("user", user);
			request.getRequestDispatcher("/WEB-INF/Views/UserManagement.jsp").forward(request, response);
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
				request.setAttribute("message", "User Created Successfully");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", true);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "User Creation Failed");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", false);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			}
			break;
		case "changepassword":
			if (userManagement.changePassword(user.getId(), request.getParameter("oldPass"),
					request.getParameter("newPass"))) {
				request.setAttribute("message", "Password Changed Successfully");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", true);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "Password Change Failed");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", false);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			}
			break;
		case "doneEditUser":
			userToEdit = new User(request.getParameter("id"), request.getParameter("role"),
					request.getParameter("fName"), request.getParameter("lName"), request.getParameter("email"),
					request.getParameter("password"));
			if (userManagement.editUser(userToEdit)) {
				request.setAttribute("message", "User Edited Successfully");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", true);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "User Edit Failed");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", false);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			}
			break;
		case "changeemail":
			if (userManagement.changeEmail(user.getId(), request.getParameter("password"),
					request.getParameter("newmail"))) {
				request.setAttribute("message", "Email Changed Successfully");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", true);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			} else {
				request.setAttribute("message", "Email Change Failed");
				request.setAttribute("returnTo", "UserManagement");
				request.setAttribute("isSuccessful", true);
				request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
			}
			break;
		default:
			request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
		}
	}
}
