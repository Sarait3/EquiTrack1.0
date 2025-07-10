package com.equitrack.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;

import com.equitrack.service.ConfirmationPageBuilder;
import com.equitrack.service.EditEquipmentBuilder;

/**
 * Servlet that handles editing of existing equipment
 * 
 * Only users with the 'Admin' and 'Manager' role are allowed to access this functionality
 * 
 */
@WebServlet("/EditEquipment")
@MultipartConfig
public class EditEquipmentServlet extends HttpServlet {

	/**
	 * Handles GET requests to display the equipment edit form Validates user
	 * session and role before proceeding
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			// Refresh user info from database
			UserDao userDao = new UserDao();
			user = userDao.getUserById(user.getId());
			request.getSession().setAttribute("currentUser", user);
		}

		// Check if user has admin access
		if (!"Admin".equalsIgnoreCase(user.getRole())) {
			response.sendRedirect("AccessDenied");
			return;
		}

		EquipmentDao equipmentDao = new EquipmentDao();
		String equipmentId = request.getParameter("id");
		Equipment equipment = equipmentDao.getEquipment(equipmentId);

		if (equipment == null) {
			response.getWriter().write("<h1>Equipment not found.</h1>");
			return;
		}

		// Build and display the edit form page
		EditEquipmentBuilder builder = new EditEquipmentBuilder(user, equipment);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}

	/**
	 * Handles POST requests to update an existing equipment Processes form input
	 * and image upload
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Fetch equipment from DB using ID from form
		EquipmentDao dao = new EquipmentDao();
		String id = request.getParameter("id");
		Equipment equipment = dao.getEquipment(id);

		// Update equipment from form fields
		equipment.setName(request.getParameter("name"));
		equipment.setLocation(request.getParameter("location"));
		equipment.setNotes(request.getParameter("notes"));
		equipment.setOperational(Boolean.parseBoolean(request.getParameter("isOperational")));
		// Handle image file upload
		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			// Save uploaded file to 'images' directory
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			equipment.setImagePath("images/" + fileName);
		}

		// Save updated equipment to database
		dao.updateEquipment(equipment);

		// Display confirmation page
		String message = "Equipment updated successfully";

		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "ListView", true);

		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}

}
