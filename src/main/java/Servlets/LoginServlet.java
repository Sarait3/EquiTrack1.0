package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Entities.User;
import Service.LoginService;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PrintWriter writer = null;
	LoginService login = null;
	HttpSession session;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		writer = response.getWriter();
		login = new LoginService();
		session = request.getSession();
		
		if (session.getAttribute("user") == null) {
			writer.write(login.loginPage(true));
		} else {
			writer.write("login successful");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		login = new LoginService();
		writer = response.getWriter();

		String email = request.getParameter("email");
		String password = request.getParameter("password");
		User user = login.validateLogin(email, password);
		
		if (user == null) {
			writer.write(login.loginPage(false));
		} else {
			login.createSession(user, request);
			writer.write("login successful");
		}
	}

}
