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
 * This servlet handles displaying the list of equipment It supports search and
 * status-based filtering
 * 
 */
@WebServlet("/ListView")
public class ListViewServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		// Refresh user from DB
		user = new UserDao().getUserById(user.getId());
		request.getSession().setAttribute("user", user);

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
		ArrayList<Equipment> equipmentList = new ArrayList<>(dao.getAllEquipment().values());

		String searchInput = request.getParameter("searchInput");
		String statusFilter = request.getParameter("statusFilter");

		equipmentList = EquipmentService.filterEquipment(equipmentList, searchInput, statusFilter);


		request.setAttribute("equipmentList", equipmentList);
		request.setAttribute("user", user);
		request.setAttribute("sidebarStrategy", strategy);
		request.setAttribute("searchInput", searchInput);
		request.setAttribute("statusFilter", statusFilter);
		request.getRequestDispatcher("/WEB-INF/Views/ListView.jsp").forward(request, response);

	}
}
