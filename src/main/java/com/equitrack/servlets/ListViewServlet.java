package com.equitrack.servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.model.*;
import com.equitrack.service.AdminPageStrategy;
import com.equitrack.service.EquipmentService;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.PageRoleStrategy;
import com.equitrack.service.RegularUserPageStrategy;
import com.equitrack.dao.*;

/**
 * Displays the equipment list and supports search and status filtering.
 */
@WebServlet("/ListView")
public class ListViewServlet extends HttpServlet {

	/**
	 * Renders the equipment list for the current user with optional filters
	 *
	 * @param request  HTTP request
	 * @param response HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			// Not authenticated: go to login
			response.sendRedirect("Login");
			return;
		}

		// Refresh user from DB to reflect any role or profile updates
		user = new UserDao().getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		// Choose sidebar/permissions strategy by role
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

		EquipmentDao dao = new EquipmentDao();
		// Copy to a mutable list for filtering while preserving order
		ArrayList<Equipment> equipmentList = new ArrayList<>(dao.getAllEquipment().values());

		// Optional filters
		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

		// Apply filters using shared service logic
		equipmentList = EquipmentService.filterEquipment(equipmentList, searchInput, statusFilter);

		// Provide data to JSP
		request.setAttribute("equipmentList", equipmentList);
		request.setAttribute("user", user);
		request.setAttribute("sidebarStrategy", strategy);
		request.setAttribute("searchInput", searchInput);
		request.setAttribute("statusFilter", statusFilter);

		request.getRequestDispatcher("/WEB-INF/Views/ListView.jsp").forward(request, response);
	}
}
