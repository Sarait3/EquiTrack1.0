package com.equitrack.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

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

	CheckoutService checkout = new CheckoutService();

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

		// Redirect to login page if the user is not authenticated
		if (user == null) {
			response.sendRedirect("Login");
			return;
		}

		// Generate and send the checkout form HTML
		response.getWriter().write(checkout.checkoutForm(user, request.getParameter("id")));
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

		// Retrieve form parameters
		String itemId = request.getParameter("itemId");
		int userId = Integer.valueOf(request.getParameter("userId"));
		Date checkoutDate = Date.valueOf(request.getParameter("checkoutDate"));
		Date returnDate = Date.valueOf(request.getParameter("returnDate"));

		// Log checkout in the system
		checkout.checkoutItem(itemId, userId, checkoutDate, returnDate);

		// Display confirmation page
		ConfirmationPageBuilder builder = new ConfirmationPageBuilder("Checkout Successful");
		String html = builder.buildPage();
		response.setContentType("text/html");
		response.getWriter().write(html);
	}

}
