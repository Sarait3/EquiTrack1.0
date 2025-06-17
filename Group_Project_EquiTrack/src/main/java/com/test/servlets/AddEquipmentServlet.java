package com.test.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.test.dao.EquipmentDao;
import com.test.dao.UserDao;
import com.test.model.Equipment;
import com.test.model.User;

import builders.AddEquipmentBuilder;
import builders.ConfirmationPageBuilder;

@WebServlet("/AddEquipment")
@MultipartConfig
public class AddEquipmentServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = (User) request.getSession().getAttribute("currentUser");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			try {
				UserDao dao = new UserDao();
				user = dao.getUserById(user.getId());
				request.getSession().setAttribute("currentUser", user);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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
		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			imagePath = "images/" + fileName;
		}
		String notes = request.getParameter("notes");
		boolean isAvailable = Boolean.parseBoolean(request.getParameter("isAvailable"));

		Equipment equipment = new Equipment(name, isAvailable, location, imagePath, notes);
		dao.createEquipment(equipment);
		String message = "Equipment created successfully";
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}

}
