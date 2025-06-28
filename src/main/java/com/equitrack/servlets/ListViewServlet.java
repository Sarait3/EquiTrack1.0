package com.equitrack.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.model.*;

import com.equitrack.service.ListViewBuilder;

import com.equitrack.dao.*;

/**
 * This servlet handles displaying the list of equipment It supports search and
 * status-based filtering
 * 
 */
@WebServlet("/ListView")
public class ListViewServlet extends HttpServlet {

	/**
	 * Handles GET requests to display the list of equipment Applies optional
	 * filters for search keywords and availability status
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check user authentication
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			// Refresh user info from the database
			UserDao userDao = new UserDao();
			user = userDao.getUserById(user.getId());
			request.getSession().setAttribute("user", user);
		}

		// Retrieve all equipment from the database
		EquipmentDao equipmentDao = new EquipmentDao();
		ArrayList<Equipment> equipmentList = new ArrayList<>(equipmentDao.getAllEquipment().values());

		// Get filter inputs from request
		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

		// Apply search filter if not null
		if (searchInput != null && !searchInput.trim().isEmpty()) {
			String lowerSearch = searchInput.toLowerCase();
			ArrayList<Equipment> filteredList = new ArrayList<>();
			for (Equipment eq : equipmentList) {
				if (eq.getName().toLowerCase().contains(lowerSearch) || eq.getId().toLowerCase().contains(lowerSearch)
						|| eq.getLocation().toLowerCase().contains(lowerSearch)) {
					filteredList.add(eq);
				}
			}
			equipmentList = filteredList;
		}

		// Apply status filter if not null
		if (statusFilter != null && !statusFilter.trim().isEmpty()) {
			boolean available = statusFilter.equalsIgnoreCase("available");
			ArrayList<Equipment> filteredList = new ArrayList<>();
			for (Equipment eq : equipmentList) {
				if (eq.isAvailable() == available) {
					filteredList.add(eq);
				}
			}
			equipmentList = filteredList;
		}

		// Display the list of equipment
		ListViewBuilder builder = new ListViewBuilder(user, equipmentList, searchInput, statusFilter);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
