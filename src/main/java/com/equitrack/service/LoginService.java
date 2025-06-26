package com.equitrack.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.equitrack.dao.UserDao;
import com.equitrack.model.*;

public class LoginService {


	public String loginPage(boolean loginFailed) {
		String errormsg = "";

		errormsg = loginFailed ? "" : "<p>Login failed, please try again</p>";

		String page = String.format("<!DOCTYPE html><html lang='en'>\r\n"
				+ "<html>\r\n"
				+ "    <head>\r\n"
				+ "        <meta charset='UTF-8'>\r\n"
				+ "        <meta name='viewport' content='width=device-width, initial-scale=1.0'>\r\n"
				+ "        <title>EquiTrack</title>\r\n"
				+ "			<link rel='stylesheet' href='css/style.css'>\r\n"				
				+ "	   <style>\r\n"
				+ "        p {\r\n"
				+ "            color: red;\r\n"
				+ "        }\r\n"
				+ "    </style>"
				+ "    </head>\r\n"
				+ "    <body>\r\n"
				+ "        <div class='header'>\r\n"
				+ "            <h1>Login</h1>\r\n"
				+ "            <div class='header-content'>\r\n"
				+ "            </div>\r\n"
				+ "        </div>\r\n"
				+ "        <form class='container-detail edit-form' action='Login' method='POST'>\r\n"
				+ "        <label for='email'>Email:</label>\r\n"
				+ "        <input type='text' name='email' id='email' required>\r\n"
				+ "        <label for='password'>Password:</label>\r\n"
				+ "        <input type='password' name='password' id='password' required>\r\n"
				+ "		   <div class='form-buttons'>"
				+ "        <button type='submit'>Login</button></div>\r\n"
				+ "        %s\r\n"
				+ "        </form>\r\n"
				+ "    </body>\r\n"
				+ "</html>",
				errormsg
				);

		return page;

	}

	public User validateLogin(String email, String password) {
		UserDao dao = new UserDao();
		User user = dao.getUserByEmail(email);

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
