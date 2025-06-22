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

/**
 * Servlet implementation class CheckoutFormServlet
 */
@WebServlet("/CheckoutForm")
public class CheckoutFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	CheckoutService checkout = new CheckoutService();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckoutFormServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if (user == null) {
			response.sendRedirect("Login");
		}
		
		PrintWriter writer = response.getWriter();
		
		writer.write(checkout.checkoutForm(user, request.getParameter("id")));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		
		String itemId = request.getParameter("itemId");
		int userId = Integer.valueOf(request.getParameter("userId"));
		Date checkoutDate = Date.valueOf(request.getParameter("checkoutDate"));
		
		checkout.checkoutItem(itemId, userId, checkoutDate);
		
		writer.write("Item Checked Out");
	}

}
