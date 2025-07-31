package com.equitrack.servlets;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.equitrack.model.*;
import com.equitrack.service.AdminPageStrategy;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.PageRoleStrategy;
import com.equitrack.service.RegularUserPageStrategy;
import com.equitrack.dao.*;

/**
 * Servlet for displaying a list of checkout requests.
 * 
 */
@WebServlet("/RequestsList")
public class RequestsListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}
		
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

		UserDao userDao = new UserDao();
		user = userDao.getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		RequestDao requestDao = new RequestDao();
		ArrayList<Request> requestsList;

		if (user.getRole().equalsIgnoreCase("Regular")) {
			requestsList = new ArrayList<>(requestDao.getRequestsByUserId(user.getId()).values());
		} else {
			requestsList = new ArrayList<>(requestDao.getAllRequests().values());
		}

		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

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

		if (statusFilter != null && !statusFilter.trim().isEmpty()) {
			ArrayList<Request> filtered = new ArrayList<>();
			for (Request req : requestsList) {
				if (req.getStatus().equalsIgnoreCase(statusFilter)) {
					filtered.add(req);
				}
			}
			requestsList = filtered;
		}

		for (Request req : requestsList) {
			User reqUser = requestDao.getUserForRequest(req.getId());
			Equipment equipment = requestDao.getEquipmentForRequest(req.getId());
			request.setAttribute("reqUser_" + req.getId(), reqUser);
			request.setAttribute("equipment_" + req.getId(), equipment);
		}

		request.setAttribute("user", user);
		request.setAttribute("sidebarStrategy", strategy);
		request.setAttribute("requestsList", requestsList);
		request.setAttribute("searchInput", searchInput);
		request.setAttribute("statusFilter", statusFilter);

		request.getRequestDispatcher("/WEB-INF/Views/RequestsList.jsp").forward(request, response);
	}
}
