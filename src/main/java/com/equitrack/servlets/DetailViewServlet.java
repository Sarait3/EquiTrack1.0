package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;

import com.equitrack.service.ConfirmationPageBuilder;
import com.equitrack.service.DetailViewBuilder;

/**
 * Servlet that handles displaying the detail view for a specific equipment item
 * Based on the user role and action requested, it can also perform actions such
 * as deleting, returning, checking out, or redirecting to edit equipment form
 * 
 */
@WebServlet("/DetailView")
public class DetailViewServlet extends HttpServlet {

	/**
	 * Handles GET requests to display detailed information about a piece of
	 * equipment Supports admin actions like delete, return, checkout, and edit
	 *
	 * @param request  the HttpServletRequest object containing client request data
	 * @param response the HttpServletResponse object used to respond to the client
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is logged in
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			// Refresh user from DB to reflect latest data
			UserDao userDao = new UserDao();
			user = userDao.getUserById(user.getId());
			request.getSession().setAttribute("user", user);
		}

		// Retrieve equipment by ID
		EquipmentDao equipmentDao = new EquipmentDao();
		String equipmentId = request.getParameter("id");
		Equipment equipment = equipmentDao.getEquipment(equipmentId);

		// Process optional action parameter
		String action = request.getParameter("action");
		if ("delete".equals(action)) {
			// Delete the equipment and show confirmation
			equipmentDao.deleteEquipment(equipmentId);
			String message = "Equipment deleted successfully";
			ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message);
			String html = builder.buildPage();
			response.setContentType("text/html");
			response.getWriter().write(html);
			return;
		} else if ("return".equals(action)) {
			// Mark equipment as available and update
			equipment.setAvailbale(true);
			equipmentDao.updateEquipment(equipment);
			response.sendRedirect("DetailView?id=" + equipmentId);
		} else if ("checkout".equals(action)) {
			// Redirect to checkout form
			response.sendRedirect("CheckoutForm?id=" + equipmentId);
		} else if ("edit".equals(action)) {
			// Redirect to edit form
			response.sendRedirect("EditEquipment");
		}

		// Display equipment details
		DetailViewBuilder builder = new DetailViewBuilder(user, equipment);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);

	}
}
