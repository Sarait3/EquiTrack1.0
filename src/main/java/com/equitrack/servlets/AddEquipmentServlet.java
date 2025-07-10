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

import com.equitrack.service.AddEquipmentBuilder;
import com.equitrack.service.ConfirmationPageBuilder;

/**
 * Servlet that handles both displaying the equipment creation form (GET) and
 * processing new equipment submissions (POST) Allows admins to add new
 * equipment
 */
@WebServlet("/AddEquipment")
@MultipartConfig // Enables file upload handling
public class AddEquipmentServlet extends HttpServlet {

	/**
	 * Handles GET requests to display the equipment creation form
	 *
	 * @param request  the HTTP servlet request
	 * @param response the HTTP servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if user is authenticated
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			// Refresh user data from the database to ensure it's up-to-date
			UserDao dao = new UserDao();
			user = dao.getUserById(user.getId());
			request.getSession().setAttribute("user", user);
		}

		// Build and display the Add Equipment form
		AddEquipmentBuilder builder = new AddEquipmentBuilder(user);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}

	/**
	 * Handles POST requests to create new equipment using form submission
	 *
	 * @param request  the HTTP servlet request containing form data
	 * @param response the HTTP servlet response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EquipmentDao dao = new EquipmentDao();
		String imagePath = null;

		// Extract form fields
		String name = request.getParameter("name");
		String location = request.getParameter("location");
		String notes = request.getParameter("notes");
		boolean isOperational = Boolean.parseBoolean(request.getParameter("isOperational"));

		// Handle file upload (equipment image)
		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			imagePath = "images/" + fileName;
		}

		// Create and save the equipment in the database
		Equipment equipment = new Equipment(name, isOperational, location, imagePath, notes, null);
		dao.createEquipment(equipment);

		// Show confirmation page
		String message = "Equipment created successfully";
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "ListView", true);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}

}
