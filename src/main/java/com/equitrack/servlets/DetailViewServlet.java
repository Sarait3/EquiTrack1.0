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

@WebServlet("/DetailView")
public class DetailViewServlet extends HttpServlet {

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
		EquipmentDao dao = new EquipmentDao();
		String equipmentId = request.getParameter("id");
		Equipment equipment = dao.getEquipment(equipmentId);
		String action = request.getParameter("action");

		if ("delete".equals(action)) {
			dao.deleteEquipment(equipmentId);
			String message = "Equipment deleted successfully";
			ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message);
			String html = builder.buildPage();
			response.setContentType("text/html");
			response.getWriter().write(html);
			return;
		} else if ("return".equals(action)) {
			equipment.setAvailbale(true);
			dao.updateEquipment(equipment);
			response.sendRedirect("DetailView?id=" + equipmentId);
		} else if ("checkout".equals(action)) {
			response.sendRedirect("CheckoutForm?id=" + equipmentId);
		} else if ("edit".equals(action)) {
			response.sendRedirect("EditEquipment");
		}

		DetailViewBuilder builder = new DetailViewBuilder(user, equipment);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);

	}
}
