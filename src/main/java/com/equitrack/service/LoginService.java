package com.equitrack.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.equitrack.dao.UserDao;
import com.equitrack.model.*;

/**
 * Handles login-related logic for the EquiTrack application Builds the HTML
 * content for the login page
 */
public class LoginService {

	/**
	 * Generates the login page HTML.
	 *
	 * @param loginFailed whether the login attempt failed
	 * @return HTML content for the login page
	 */
	public String loginPage(boolean firstTimeLogin) {
		String errormsg = firstTimeLogin ? "" : "Login failed, please try again";

		FormBuilder builder = new FormBuilder();

		builder.setTitle("Login").setAction("Login").setMethod("post").addRequiredInput("text", "Email:", "email")
				.addRequiredInput("password", "Password:", "password").addReset().addError(errormsg);

		return builder.createForm(true, false);

	}

	/**
	 * Validates user credentials
	 *
	 * @param email    the user's email
	 * @param password the user's password
	 * @return the User object if valid, null otherwise
	 */
	public User validateLogin(String email, String password) {
		UserDao dao = new UserDao();

		User user = dao.getUserByEmail(email);

		// Hash the password before comparing
		password = hashPassword(password);

		if (user != null) {
			if (user.getPassword().equals(password)) {
				return user;
			}
		}

		return null;
	}

	/**
	 * Creates a session for the authenticated user
	 *
	 * @param user    the logged-in user
	 * @param request the HTTP servlet request
	 */
	public void createSession(User user, HttpServletRequest request) {

		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		session.setMaxInactiveInterval(43200);
	}

	/**
	 * Hashes the given password using SHA-256
	 *
	 * @param password the plain text password to hash
	 * @return the hashed password in hexadecimal string format
	 */
	public String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			byte[] encodedhash = digest.digest(password.getBytes());

			StringBuilder sb = new StringBuilder();

			for (byte b : encodedhash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					sb.append('0');
				}
				sb.append(hex);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return password;
	}
}