package com.equitrack.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LogoutServlet handles the logout process for users
 * 
 */
@WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {

	/**
	 * Handles GET requests to log the user out It invalidates the current session
	 * and redirects to the login page
	 *
	 * @param request  the HttpServletRequest from the client
	 * @param response the HttpServletResponse to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getSession().invalidate();
		response.sendRedirect("Login");
	}

}