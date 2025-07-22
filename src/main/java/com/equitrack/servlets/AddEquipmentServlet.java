package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;
import com.equitrack.service.AccessDeniedBuilder;
import com.equitrack.service.AddEquipmentBuilder;
import com.equitrack.service.ConfirmationPageBuilder;

/**
 * Servlet that handles displaying and submitting the form to add new equipment
 * 
 */
@WebServlet("/AddEquipment")
@MultipartConfig
public class AddEquipmentServlet extends HttpServlet {

	/**
	 * Handles GET requests by showing the Add Equipment form Only managers and
	 * admins are allowed to access it
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");

		// Redirect to login if not logged in
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			// Refresh user data from the database
			UserDao dao = new UserDao();
			user = dao.getUserById(user.getId());
			request.getSession().setAttribute("user", user);
		}

		// Block regular users
		if (user.getRole().equalsIgnoreCase("Regular")) {
			response.setContentType("text/html");
			response.getWriter().write(new AccessDeniedBuilder().buildPage());
			return;
		}

		// Show form to add new equipment
		AddEquipmentBuilder builder = new AddEquipmentBuilder(user);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}

	/**
	 * Handles POST requests by creating a new equipment item using form data and
	 * uploaded image file
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EquipmentDao dao = new EquipmentDao();
		String imagePath = null;

		String name = request.getParameter("name");
		String location = request.getParameter("location");
		String notes = request.getParameter("notes");
		boolean isOperational = Boolean.parseBoolean(request.getParameter("isOperational"));

		// Handle uploaded image file
		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			imagePath = "images/" + fileName;
		}

		// Create Equipment object using builder
		Equipment equipment = new Equipment.Builder().setName(name).setOperational(isOperational).setLocation(location)
				.setImagePath(imagePath).setNotes(notes).build();

		// Save to database
		dao.createEquipment(equipment);

		// Show confirmation page
		String message = "Equipment created successfully";
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "ListView", true);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
