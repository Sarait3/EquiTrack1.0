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
import com.equitrack.service.AccessDeniedBuilder;
import com.equitrack.service.AddEquipmentBuilder;
import com.equitrack.service.ConfirmationPageBuilder;

@WebServlet("/AddEquipment")
@MultipartConfig
public class AddEquipmentServlet extends HttpServlet {

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
		
	    if (user.getRole().equalsIgnoreCase("Regular")) {
	    	response.setContentType("text/html");
			response.getWriter().write(new AccessDeniedBuilder().buildPage());
			return;
	    }

		AddEquipmentBuilder builder = new AddEquipmentBuilder(user);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EquipmentDao dao = new EquipmentDao();
		String imagePath = null;

		String name = request.getParameter("name");
		String location = request.getParameter("location");
		String notes = request.getParameter("notes");
		boolean isOperational = Boolean.parseBoolean(request.getParameter("isOperational"));

		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			imagePath = "images/" + fileName;
		}

		Equipment equipment = new Equipment(name, isOperational, location, imagePath, notes, null);
		dao.createEquipment(equipment);

		String message = "Equipment created successfully";
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "ListView", true);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
