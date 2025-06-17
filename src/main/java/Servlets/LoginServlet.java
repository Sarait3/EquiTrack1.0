package Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Service.LoginService;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private PrintWriter writer = null;
       
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
		
		writer.write(
				"<html>"
				+ "<head title=\"EquiTrack\">"
				+ "<body>"
				+ "<h1>Login</h1>"
				+ "<form method=\"post\">"
				+ "<label for=\"email\">Email:</label><br>"
				+ "<input type=\"text\" name=\"email\" id=\"email\" required><br>"
				+ "<label for=\"password\">Password:</label><br>"
				+ "<input type=\"password\" name=\"password\" id=\"password\" required><br>"
				+ "<input type=\"submit\" value=\"Login\">"
				+ "</form>"
				+ "</body>"
				+ "</head>"
				+ "</html>"
				);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LoginService login = new LoginService();
		writer = response.getWriter();
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		if (!login.validateLogin(email, password)) {
			writer.write("invalid login");
		}else {
			writer.write("login successful");
		}
	}

}
