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
import com.equitrack.service.RequestsListBuilder;
import com.equitrack.dao.*;


@WebServlet("/RequestsList")
public class RequestsListServlet extends HttpServlet {

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

		// Retrieve all requests from the database
		RequestDao requestDao = new RequestDao();
		ArrayList<Request> requestsList;
		
		if (user.getRole().equalsIgnoreCase("Regular")) {
			requestsList = new ArrayList<>(requestDao.getRequestsByUserId(user.getId()).values());
		}
		else {
			requestsList = new ArrayList<>(requestDao.getAllRequests().values());
		}
		

		// Get filter inputs from request
		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

		// Apply search filter if not null
		if (searchInput != null && !searchInput.trim().isEmpty()) {
			String lowerSearch = searchInput.toLowerCase();
			ArrayList<Request> filteredList = new ArrayList<>();
			for (Request req : requestsList) {
				User reqUser = requestDao.getUserForRequest(req.getId());
				Equipment reqEquipment = requestDao.getEquipmentForRequest(req.getId());
				if (req.getId().toLowerCase().contains(lowerSearch) || reqUser.getFName().toLowerCase().contains(lowerSearch) || reqUser.getLName().toLowerCase().contains(lowerSearch)
						|| reqEquipment.getName().toLowerCase().contains(lowerSearch)) {
					filteredList.add(req);
				}
			}
			requestsList = filteredList;
		}

		// Apply status filter if not null
		if (statusFilter != null && !statusFilter.trim().isEmpty()) {
			ArrayList<Request> filteredList = new ArrayList<>();
			for (Request req : requestsList) {
				if (req.getStatus().equalsIgnoreCase(statusFilter)) {
					filteredList.add(req);
				}
			}
			requestsList = filteredList;
		}

		RequestsListBuilder builder = new RequestsListBuilder(user, requestsList, searchInput, statusFilter);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
