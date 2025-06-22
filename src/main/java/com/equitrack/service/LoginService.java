package com.equitrack.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.equitrack.dao.DBAccessor;
import com.equitrack.model.*;

public class LoginService {


	public String loginPage(boolean loginFailed) {
		String errormsg = "";

		errormsg = loginFailed ? "" : "<p>Login failed, please try again</p>";

		String page = String.format("<html>"
				+ "<head title=\"EquiTrack\">"
				+ "<style>p {color: red;}</style>"
				+ "</head>"
				+ "<body>"
				+ "<h1>Login</h1>"
				+ "<form method=\"post\">"
				+ "<label for=\"email\">Email:</label><br>"
				+ "<input type=\"text\" name=\"email\" id=\"email\" required><br>"
				+ "<label for=\"password\">Password:</label><br>"
				+ "<input type=\"password\" name=\"password\" id=\"password\" required><br>"
				+ "<input type=\"submit\" value=\"Login\"><br>"
				+ "%s"
				+ "</form>"
				+ "</body>"
				+ "</html>",
				errormsg
				);

		return page;

	}

	public User validateLogin(String email, String password) {

		User user = DBAccessor.getUser("email", email);

		if(user != null) {
			if (user.getPassword().equals(password)) {
				return user;
			}
		}

		return null;	
	}

	public void createSession(User user, HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		session.setMaxInactiveInterval(43200);
	}
}
