package com.equitrack.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import com.equitrack.dao.EquipmentDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;
import com.equitrack.service.FormBuilder;

/**
 * Servlet that handles editing of existing equipment
 * 
 * Only users with the 'Admin' and 'Manager' role are allowed to access this
 * functionality
 * 
 */
@WebServlet("/EditEquipment")
@MultipartConfig
public class EditEquipmentServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		if (user.getRole().equalsIgnoreCase("Regular")) {
			request.setAttribute("message", "Access Denied: You do not have permission to access this page.");
			request.setAttribute("success", false);
			request.getRequestDispatcher("/WEB-INF/Views/AccessDenied.jsp").forward(request, response);
			return;
		}

		String id = request.getParameter("id");
		EquipmentDao dao = new EquipmentDao();
		Equipment equipment = dao.getEquipment(id);

		if (equipment == null) {
			response.getWriter().write("<h1>Equipment not found.</h1>");
			return;
		}

		// Build form using FormBuilder
		FormBuilder builder = new FormBuilder();
		String status = equipment.isOperationalString();

		builder.setTitle("Edit Equipment").setAction("EditEquipment").setMethod("post")
				.addHiddenInput("id", equipment.getId()).addRequiredInput("text", "Name:", "name", equipment.getName())
				.addRequiredInput("text", "Location:", "location", equipment.getLocation())
				.addFileInput("Image:", "imageFile", "image/*")
				.addCustomLine(
						"<label for='isOperational'>Status:</label><select id='isOperational' name='isOperational'>"
								+ "<option value='true'" + (equipment.isOperational() ? " selected" : "")
								+ ">Operational</option>" + "<option value='false'"
								+ (!equipment.isOperational() ? " selected" : "") + ">Out Of Service</option>"
								+ "</select>")
				.addInput("textarea", "Notes:", "notes", equipment.getNotes() != null ? equipment.getNotes() : "")
				.removeDefaultSubmit()
				.addCustomLine("<div class='form-buttons'>" + "<button type='submit'>Save Changes</button>"
						+ "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

		String formHtml = builder.createForm(false, true);

		// Pass data to JSP
		request.setAttribute("formHtml", formHtml);
		request.setAttribute("user", user);
		request.setAttribute("equipment", equipment);
		request.getRequestDispatcher("/WEB-INF/Views/EditEquipment.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		EquipmentDao dao = new EquipmentDao();
		String id = request.getParameter("id");
		Equipment equipment = dao.getEquipment(id);

		equipment.setName(request.getParameter("name"));
		equipment.setLocation(request.getParameter("location"));
		equipment.setNotes(request.getParameter("notes"));
		equipment.setOperational(Boolean.parseBoolean(request.getParameter("isOperational")));

		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			equipment.setImagePath("images/" + fileName);
		}

		dao.updateEquipment(equipment);

		request.setAttribute("message", "Equipment updated successfully");
		request.setAttribute("isSuccessful", true);
		request.setAttribute("returnTo", "ListView");
		
		request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
	}
}
