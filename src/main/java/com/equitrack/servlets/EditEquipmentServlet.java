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

@WebServlet("/EditEquipment")
@MultipartConfig
public class EditEquipmentServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("currentUser");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		} else {
			UserDao dao = new UserDao();
			user = dao.getUserById(user.getId());
			request.getSession().setAttribute("currentUser", user);
		}

		EquipmentDao dao = new EquipmentDao();
		String equipmentId = request.getParameter("id");
		Equipment equipment = dao.getEquipment(equipmentId);

		if (equipment == null) {
			response.getWriter().write("<h1>Equipment not found.</h1>");
			return;
		}

		EditEquipmentBuilder builder = new EditEquipmentBuilder(user, equipment);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EquipmentDao dao = new EquipmentDao();
		String id = request.getParameter("id");
		System.out.println("Equipment ID: " + id);
		Equipment equipment = dao.getEquipment(id);

		equipment.setName(request.getParameter("name"));
		equipment.setLocation(request.getParameter("location"));
		Part filePart = request.getPart("imageFile");
		if (filePart != null && filePart.getSize() > 0) {
			String fileName = filePart.getSubmittedFileName();
			filePart.write(getServletContext().getRealPath("/") + "images/" + fileName);
			equipment.setImagePath("images/" + fileName);
		}
		equipment.setNotes(request.getParameter("notes"));
		equipment.setAvailbale(Boolean.parseBoolean(request.getParameter("isAvailable")));

		dao.updateEquipment(equipment);
		String message = "Equipment updated successfully";
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}

}
