package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.model.User;
import com.equitrack.service.UserManagement.UserManagementService;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		User user = (User)request.getSession().getAttribute("user");
//
//		if (user == null) {
//			response.sendRedirect("Login");
//			return;
//		}

		User user = new User("admin", "Matt", "Sicard", "matt@example.com", "myPass");
		String action = request.getParameter("action");

		if (action != null) {
			switch (action) {
			case "changepassword":
				break;
			case "changeemail":
				break;
			default:
				response.getWriter().write(new UserManagementService(user).buildPage());
			}
		} else {
			response.getWriter().write(new UserManagementService(user).buildPage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
