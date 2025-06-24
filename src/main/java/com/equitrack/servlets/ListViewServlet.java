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

@WebServlet("/ListView")
public class ListViewServlet extends HttpServlet {
	private List<Equipment> allEquipment = new ArrayList<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			UserDao dao = new UserDao();
			user = dao.getUserById(user.getId());
			request.getSession().setAttribute("user", user);
		}

		EquipmentDao dao = new EquipmentDao();
		ArrayList<Equipment> equipmentList = new ArrayList<>(dao.getAllEquipment().values());

		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

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

		ListViewBuilder builder = new ListViewBuilder(user, equipmentList, searchInput, statusFilter);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
