package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;
import com.equitrack.service.AdminPageStrategy;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.PageRoleStrategy;
import com.equitrack.service.RegularUserPageStrategy;

/**
 * Displays equipment details and handles simple actions from the detail view
 *
 */
@WebServlet("/DetailView")
public class DetailViewServlet extends HttpServlet {

	/**
	 * Shows the equipment detail page or performs an action, depending on the
	 * "action" parameter
	 *
	 * @param request  HTTP request (expects "id" param for the equipment, optional
	 *                 "action")
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

		// Refresh user from DB to reflect any recent changes
		user = new UserDao().getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		// Choose sidebar/permissions strategy based on role
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

		String equipmentId = request.getParameter("id");
		Equipment equipment = new EquipmentDao().getEquipment(equipmentId);

		String action = request.getParameter("action");
		if ("delete".equals(action)) {
			// Delete and show a confirmation message
			new EquipmentDao().deleteEquipment(equipmentId);
			request.setAttribute("message", "Equipment deleted successfully");
			request.setAttribute("returnTo", "ListView");
			request.setAttribute("isSuccessful", true);
			request.getRequestDispatcher("confirmation.jsp").forward(request, response);
			return;

		} else if ("backInService".equals(action)) {
			// Mark equipment as operational and reload detail page
			equipment.setOperational(true);
			new EquipmentDao().updateEquipment(equipment);
			response.sendRedirect("DetailView?id=" + equipmentId);
			return;

		} else if ("checkout".equals(action)) {
			// Go to checkout form for this item
			response.sendRedirect("CheckoutForm?id=" + equipmentId);
			return;

		} else if ("edit".equals(action)) {
			// Go to edit page
			response.sendRedirect("EditEquipment?id=" + equipmentId);
			return;
		}

		// Default: render the detail view
		request.setAttribute("user", user);
		request.setAttribute("equipment", equipment);
		request.setAttribute("sidebarStrategy", strategy);

		getServletContext().getRequestDispatcher("/WEB-INF/Views/DetailView.jsp").forward(request, response);
	}
}
