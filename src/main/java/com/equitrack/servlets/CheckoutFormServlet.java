package com.equitrack.servlets;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.equitrack.model.User;
import com.equitrack.service.CheckoutService;
import com.equitrack.service.ConfirmationPageBuilder;

@WebServlet("/CheckoutForm")
public class CheckoutFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String itemId = request.getParameter("id");

		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		CheckoutService checkout = new CheckoutService(user, itemId);
		response.getWriter().write(checkout.buildPage());
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String userId = user.getId();

		String itemId = request.getParameter("itemId");
		String location = request.getParameter("location");
		String notes = request.getParameter("notes");
		LocalDate checkoutDate = LocalDate.parse(request.getParameter("checkoutDate"));
		LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));

		CheckoutService checkout = new CheckoutService(user, itemId);

		if (checkout.requestCheckout(itemId, userId, location, notes, checkoutDate, returnDate)) {
			ConfirmationPageBuilder builder = new ConfirmationPageBuilder(
				"Checkout Request Submitted Successfully", "ListView", true);
			String html = builder.buildPage();
			response.setContentType("text/html");
			response.getWriter().write(html);
		} else {
			ConfirmationPageBuilder builder = new ConfirmationPageBuilder(
				"This item is not available for the requested dates", "ListView", false);
			String html = builder.buildPage();
			response.setContentType("text/html");
			response.getWriter().write(html);
		}
	}
}
