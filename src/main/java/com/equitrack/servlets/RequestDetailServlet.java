package com.equitrack.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.equitrack.dao.RequestDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;
import com.equitrack.service.AdminPageStrategy;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.PageRoleStrategy;
import com.equitrack.service.RegularUserPageStrategy;

/**
 * Servlet that handles viewing and processing checkout requests.
 * 
 * If no action is given, it displays request details.
 */
@WebServlet("/RequestDetail")
public class RequestDetailServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}
		
		PageRoleStrategy strategy;
		switch (user.getRole().toLowerCase()) {
		case "admin":
			strategy = new AdminPageStrategy();
			break;
		case "manager":
			strategy = new ManagerPageStrategy();
			break;
		default:
			strategy = new RegularUserPageStrategy();
			break;
		}

		UserDao userDao = new UserDao();
		user = userDao.getUserById(user.getId());
		request.getSession().setAttribute("user", user);

		RequestDao requestDao = new RequestDao();
		String requestId = request.getParameter("id");
		Request req = requestDao.getRequest(requestId);
		
		if (req == null) {
			response.getWriter().write("<h1>Request not found.</h1>");
			return;
		}

		String action = request.getParameter("action");

		if ("approve".equalsIgnoreCase(action)) {
			if (req.approve()) {
				requestDao.updateRequest(req);
				sendConfirmation(request, response, "Request approved successfully", true);
			} else {
				sendConfirmation(request, response, "This item is not available for the selected dates", false);
			}
			return;
		}

		if ("decline".equalsIgnoreCase(action)) {
			req.decline();
			requestDao.updateRequest(req);
			sendConfirmation(request, response, "Request declined successfully", true);
			return;
		}

		Equipment equipment = requestDao.getEquipmentForRequest(req.getId());
		User reqUser = requestDao.getUserForRequest(req.getId());

		request.setAttribute("user", user);
		request.setAttribute("sidebarStrategy", strategy);
		request.setAttribute("request", req);
		request.setAttribute("equipment", equipment);
		request.setAttribute("reqUser", reqUser);

		request.getRequestDispatcher("/WEB-INF/Views/RequestDetail.jsp").forward(request, response);
	}

	private void sendConfirmation(HttpServletRequest request, HttpServletResponse response, String message, boolean success)
			throws ServletException, IOException {
		request.setAttribute("message", message);
		request.setAttribute("isSuccessful", success);
		request.setAttribute("returnTo", "RequestsList");
		request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
	}
}
