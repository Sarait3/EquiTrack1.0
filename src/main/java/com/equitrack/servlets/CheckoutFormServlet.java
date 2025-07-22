package com.equitrack.servlets;

import java.io.IOException;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.equitrack.model.User;
import com.equitrack.service.CheckoutService;
import com.equitrack.service.ConfirmationPageBuilder;

/**
 * Servlet that handles displaying and processing the equipment checkout form
 * 
 */
@WebServlet("/CheckoutForm")
public class CheckoutFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Displays the checkout form for the selected equipment item Redirects to login
	 * if the user is not logged in
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String itemId = request.getParameter("id");

		// Redirect to login if not logged in
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		// Build and show the form
		CheckoutService checkout = new CheckoutService(user, itemId);
		response.setContentType("text/html");
		response.getWriter().write(checkout.buildPage());
	}

	/**
	 * Handles the form submission to create a new checkout request Validates date
	 * conflicts and shows confirmation or error
	 */
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

		boolean success = checkout.requestCheckout(itemId, userId, location, notes, checkoutDate, returnDate);

		String message = success ? "Checkout Request Submitted Successfully"
				: "This item is not available for the requested dates";

		ConfirmationPageBuilder builder = new ConfirmationPageBuilder(message, "ListView", success);
		String html = builder.buildPage();

		response.setContentType("text/html");
		response.getWriter().write(html);
	}
}
