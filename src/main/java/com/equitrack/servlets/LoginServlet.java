package com.equitrack.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.equitrack.model.User;
import com.equitrack.service.LoginService;

/**
 * Servlet implementation class LoginServlet It displays the login form on GET
 * requests and processes user authentication on POST requests
 */
@WebServlet({ "", "/Login" })
public class LoginServlet extends HttpServlet {

	/**
	 * Handles GET requests by checking if the user is already logged in If not, it
	 * displays the login form
	 *
	 * @param request  the HttpServletRequest object that contains the request the
	 *                 client made
	 * @param response the HttpServletResponse object that contains the response the
	 *                 servlet sends
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		LoginService login = new LoginService();
		HttpSession session = request.getSession();

		// // First visit to login page (no failed attempt), show the login form with no error message
		if (session.getAttribute("user") == null) {
			writer.write(login.loginPage(true));
		} else {
			// Already logged in, redirect to the equipment list view
			response.sendRedirect("/EquiTrack/ListView");
		}
	}

	/**
	 * Handles POST requests by validating the user's credentials If valid, it
	 * creates a session and redirects to the equipment list view, If not, it
	 * redisplays the login form with an error message
	 *
	 * @param request  the HttpServletRequest object that contains the request the
	 *                 client made
	 * @param response the HttpServletResponse object that contains the response the
	 *                 servlet sends
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LoginService login = new LoginService();
		PrintWriter writer = response.getWriter();

		// Retrieve form parameters
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		// Validate credentials
		User user = login.validateLogin(email, password);

		if (user == null) {
			// Login failed: redisplay form with error
			writer.write(login.loginPage(true));
		} else {
			// Login successful: create user session
			login.createSession(user, request);
			response.sendRedirect("/EquiTrack/ListView");
		}
	}

}
