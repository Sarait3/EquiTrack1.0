package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.RequestDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

import com.equitrack.service.ConfirmationPageBuilder;
import com.equitrack.service.DetailViewBuilder;
import com.equitrack.service.RequestDetailBuilder;

@WebServlet("/RequestDetail")
public class RequestDetailServlet extends HttpServlet {

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

		// Retrieve request by ID
		RequestDao requestDao = new RequestDao();
		String requestId = request.getParameter("id");
		Request req = requestDao.getRequest(requestId);

		String action = request.getParameter("action");
		if ("approve".equals(action)) {

			if (req.approve()) {
				requestDao.updateRequest(req);
				String message = "Request approved successfully";
				ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "RequestsList", true);
				String html = builder.buildPage();
				response.setContentType("text/html");
				response.getWriter().write(html);
			} else {
				String message = "This item is not available for the selected dates";
				ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "RequestsList", false);
				String html = builder.buildPage();
				response.setContentType("text/html");
				response.getWriter().write(html);
			}

			return;
		} else if ("decline".equals(action)) {
			req.decline();
			requestDao.updateRequest(req);
			String message = "Request declined successfully";
			ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "RequestsList", true);
			String html = builder.buildPage();
			response.setContentType("text/html");
			response.getWriter().write(html);
			return;
		}

		// Display request details
		RequestDetailBuilder builder = new RequestDetailBuilder(user, req);
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);

	}
}
