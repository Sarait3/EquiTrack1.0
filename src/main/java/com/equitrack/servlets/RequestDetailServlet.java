package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.equitrack.dao.RequestDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Request;
import com.equitrack.model.User;
import com.equitrack.service.ConfirmationPageBuilder;
import com.equitrack.service.RequestDetailBuilder;

/**
 * Servlet that handles viewing and processing checkout requests.
 * 
 * If no action is given, it displays request details.
 */
@WebServlet("/RequestDetail")
public class RequestDetailServlet extends HttpServlet {

	/**
	 * Handles GET requests to either process an action or display the request
	 * details Redirects to login if the user is not authenticated
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is logged in
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		// Refresh user from DB
		UserDao userDao = new UserDao();
		user = userDao.getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		// Get request by ID
		RequestDao requestDao = new RequestDao();
		String requestId = request.getParameter("id");
		Request req = requestDao.getRequest(requestId);

		String action = request.getParameter("action");

		// Handle approve action
		if ("approve".equalsIgnoreCase(action)) {
			if (req.approve()) {
				requestDao.updateRequest(req);
				sendConfirmation(response, "Request approved successfully", true);
			} else {
				sendConfirmation(response, "This item is not available for the selected dates", false);
			}
			return;
		}

		// Handle decline action
		if ("decline".equalsIgnoreCase(action)) {
			req.decline();
			requestDao.updateRequest(req);
			sendConfirmation(response, "Request declined successfully", true);
			return;
		}

		// No action: display request details
		RequestDetailBuilder builder = new RequestDetailBuilder(user, req);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}

	/**
	 * Sends a confirmation page with a message and redirect link
	 *
	 * @param response the HttpServletResponse to write to
	 * @param message  the message to display
	 * @param success  true if the operation was successful; false otherwise
	 */
	private void sendConfirmation(HttpServletResponse response, String message, boolean success) throws IOException {
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "RequestsList", success);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
