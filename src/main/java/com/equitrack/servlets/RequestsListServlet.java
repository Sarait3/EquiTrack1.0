package com.equitrack.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.equitrack.model.*;
import com.equitrack.service.RequestsListBuilder;
import com.equitrack.dao.*;

/**
 * Servlet for displaying a list of checkout requests.
 * 
 */
@WebServlet("/RequestsList")
public class RequestsListServlet extends HttpServlet {

	/**
	 * Handles GET requests to show the list of checkout requests based on the
	 * user's role and filters
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

		// Refresh latest user info from DB
		UserDao userDao = new UserDao();
		user = userDao.getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		// Fetch requests based on role
		RequestDao requestDao = new RequestDao();
		ArrayList<Request> requestsList;

		if (user.getRole().equalsIgnoreCase("Regular")) {
			// Regular users only see their own requests
			requestsList = new ArrayList<>(requestDao.getRequestsByUserId(user.getId()).values());
		} else {
			// Managers/Admins see all requests
			requestsList = new ArrayList<>(requestDao.getAllRequests().values());
		}

		// Get filter parameters
		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

		// Apply search filter (by ID, name, equipment name)
		if (searchInput != null && !searchInput.trim().isEmpty()) {
			String lowerSearch = searchInput.toLowerCase();
			ArrayList<Request> filtered = new ArrayList<>();
			for (Request req : requestsList) {
				User reqUser = requestDao.getUserForRequest(req.getId());
				Equipment eq = requestDao.getEquipmentForRequest(req.getId());
				if (req.getId().toLowerCase().contains(lowerSearch)
						|| reqUser.getFName().toLowerCase().contains(lowerSearch)
						|| reqUser.getLName().toLowerCase().contains(lowerSearch)
						|| eq.getName().toLowerCase().contains(lowerSearch)) {
					filtered.add(req);
				}
			}
			requestsList = filtered;
		}

		// Apply status filter (pending, approved, declined)
		if (statusFilter != null && !statusFilter.trim().isEmpty()) {
			ArrayList<Request> filtered = new ArrayList<>();
			for (Request req : requestsList) {
				if (req.getStatus().equalsIgnoreCase(statusFilter)) {
					filtered.add(req);
				}
			}
			requestsList = filtered;
		}

		// Build HTML page
		RequestsListBuilder builder = new RequestsListBuilder(user, requestsList, searchInput, statusFilter);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
