package com.equitrack.servlets;

import java.io.IOException;
import java.sql.Date;
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

/**
 * Servlet implementation class CheckoutFormServlet
 */
@WebServlet("/CheckoutForm")
public class CheckoutFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Handles GET requests to display the checkout form for a selected equipment
	 * item
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String itemId = request.getParameter("id");
		
		// Redirect to login page if the user is not authenticated
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}
		
		
		// Generate and send the checkout form HTML
		CheckoutService checkout = new CheckoutService(user, itemId);
		response.getWriter().write(checkout.buildPage());
	}

	/**
	 * Handles POST requests to process the checkout
	 *
	 * @param request  the HttpServletRequest object
	 * @param response the HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		int userId = user.getId();
		
		// Retrieve form parameters
		String itemId = request.getParameter("itemId");
		String location = request.getParameter("location");
		String notes = request.getParameter("notes");
		LocalDate checkoutDate = LocalDate.parse(request.getParameter("checkoutDate"));
		LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));
		CheckoutService checkout = new CheckoutService(user, itemId);

		// Log checkout in the system
		checkout.requestCheckout(itemId, userId, location, notes, checkoutDate, returnDate);

		// Display confirmation page
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder("Checkout Request Sybmitted Successfully", "ListView");
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}

}
