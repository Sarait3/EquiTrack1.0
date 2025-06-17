package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Service.LoginService;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PrintWriter writer = null;
	LoginService login = null;
	UUID sessionID;

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
		sessionID = null;

		try {
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("EquiTrackSession")) {
					sessionID = UUID.fromString(c.getValue());
				}
			}
		} catch (Exception e) {}

			if (sessionID == null) {
				writer.write(login.loginPage(true));
			} else {
				if (login.validateSessionID(sessionID)) {
					writer.write("login successful");
				}
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
		sessionID = null;

		sessionID = login.validateLogin(email, password);

		if (sessionID == null) {
			writer.write(login.loginPage(false));
		} else {
			Cookie sessionCookie = new Cookie("EquiTrackSession", sessionID.toString());
			response.addCookie(sessionCookie);
			writer.write("login successful");
		}
	}

}
