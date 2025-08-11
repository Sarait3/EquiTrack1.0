package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.equitrack.dao.RequestDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;
import com.equitrack.service.AdminPageStrategy;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.PageRoleStrategy;
import com.equitrack.service.RegularUserPageStrategy;

/**
 * Shows a single checkout request and, if instructed via the "action"
 * parameter, approves or declines it
 *
 * If no action is provided, the request details view is rendered
 */
@WebServlet("/RequestDetail")
public class RequestDetailServlet extends HttpServlet {

	/**
	 * Displays request details or processes an approve/decline action
	 *
	 * Requires an authenticated user stored in the session as "user"
	 *
	 * @param request  HTTP request (expects "id" and optional "action")
	 * @param response HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Authentication check
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		// Sidebar/permissions strategy based on the current role in session.
		PageRoleStrategy strategy;
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

		// Refresh user from DB to reflect any changes since login
		UserDao userDao = new UserDao();
		user = userDao.getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		// Load the request
		RequestDao requestDao = new RequestDao();
		String requestId = request.getParameter("id");
		Request req = requestDao.getRequest(requestId);

		// Guard: invalid or missing id
		if (req == null) {
			response.getWriter().write("<h1>Request not found.</h1>");
			return;
		}

		String action = request.getParameter("action");

		// Handle approve action
		if ("approve".equalsIgnoreCase(action)) {
			if (req.approve()) {
				requestDao.updateRequest(req);
				sendConfirmation(request, response, "Request approved successfully", true);
			} else {
				// Business rule: conflict or unavailability
				sendConfirmation(request, response, "This item is not available for the selected dates", false);
			}
			return;
		}

		// Handle decline action
		if ("decline".equalsIgnoreCase(action)) {
			req.decline();
			requestDao.updateRequest(req);
			sendConfirmation(request, response, "Request declined successfully", true);
			return;
		}

		// No action: show request details
		Equipment equipment = requestDao.getEquipmentForRequest(req.getId());
		User reqUser = requestDao.getUserForRequest(req.getId());

		request.setAttribute("user", user);
		request.setAttribute("sidebarStrategy", strategy);
		request.setAttribute("request", req);
		request.setAttribute("equipment", equipment);
		request.setAttribute("reqUser", reqUser);

		request.getRequestDispatcher("/WEB-INF/Views/RequestDetail.jsp").forward(request, response);
	}

	/**
	 * Forwards to a shared confirmation view with a message and success flag
	 *
	 * @param request  HTTP request
	 * @param response HTTP response
	 * @param message  message to display
	 * @param success  whether the operation succeeded
	 * @throws ServletException
	 * @throws IOException
	 */
	private void sendConfirmation(HttpServletRequest request, HttpServletResponse response, String message,
			boolean success) throws ServletException, IOException {
		request.setAttribute("message", message);
		request.setAttribute("isSuccessful", success);
		request.setAttribute("returnTo", "RequestsList");
		request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
	}
}
