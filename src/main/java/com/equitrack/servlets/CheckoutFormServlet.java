package com.equitrack.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.equitrack.model.*;
import com.equitrack.service.CheckoutService;
import com.equitrack.service.FormBuilder;
import com.equitrack.service.ManagerPageStrategy;
import com.equitrack.service.RegularUserPageStrategy;

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

	    CheckoutService service = new CheckoutService();
	    Equipment equipment = service.getEquipment(itemId);
	    Map<UUID, Request> upcomingRequests = service.getUpcomingApprovedRequests(itemId);

	    FormBuilder builder = new FormBuilder();
	    builder.setTitle("Checkout")
	           .setAction("CheckoutForm")
	           .setMethod("post")
	           .addHiddenInput("itemId", equipment.getId())
	           .addRequiredInput("date", "Checkout Date:", "checkoutDate")
	           .addRequiredInput("date", "Return Date:", "returnDate")
	           .addRequiredInput("text", "Location:", "location")
	           .addRequiredInput("text", "Explain why you need this item:", "notes")
	           .removeDefaultSubmit()
	           .addCustomLine("<div class='form-buttons'><button type='submit'>Submit</button>"
	                        + "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

	    request.setAttribute("user", user);
	    request.setAttribute("equipment", equipment);
	    request.setAttribute("upcomingRequests", upcomingRequests);
	    request.setAttribute("formHtml", builder.createForm(false, false));
	    request.setAttribute("sidebarStrategy", user.getRole().equalsIgnoreCase("Regular")
	            ? new RegularUserPageStrategy()
	            : new ManagerPageStrategy());

	    request.getRequestDispatcher("/WEB-INF/Views/CheckoutForm.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String itemId = request.getParameter("itemId");
		String location = request.getParameter("location");
		String notes = request.getParameter("notes");
		LocalDate checkoutDate = LocalDate.parse(request.getParameter("checkoutDate"));
		LocalDate returnDate = LocalDate.parse(request.getParameter("returnDate"));

		CheckoutService service = new CheckoutService();
		boolean success = service.requestCheckout(user, itemId, location, notes, checkoutDate, returnDate);

		String message = success ? "Checkout Request Submitted Successfully"
				: "This item is not available for the requested dates.";

        request.setAttribute("message", message);
        request.setAttribute("isSuccessful", success);
        request.setAttribute("returnTo", "ListView");
		request.getRequestDispatcher("/WEB-INF/Views/confirmation.jsp").forward(request, response);
	}
}
