package com.equitrack.service;

import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class AdminPageStrategy extends ManagerPageStrategy implements PageRoleStrategy {

	/**
	 * Generates the user list and buttons for editing and deleting existing users.
	 */
	@Override
	public String buildUserList() {
		UserDao dao = new UserDao();
		Map<UUID, User> users = dao.getAllUsers();

		StringBuilder html = new StringBuilder();

		html.append("<div style='text-align:center' class='action-section container-detail'>")
				.append("<h1>Manage Users</h1>").append("<table>").append("<th></th><th></th>");

		for (User user : users.values()) {
			html.append("<tr>")
					.append(String.format("<td><h2 style='color:black'>%s, %s</h2></td>", user.getLName(),
							user.getFName()))
					.append("<td><form method='post'>" + "<input hidden id='action' name='action' value='edituser'>"
							+ String.format("<input hidden id='id' name='id' value='%s'>", user.getId())
							+ "<button type='submit'>Edit User</button></form></td>");
			if (!user.getRole().equalsIgnoreCase("admin")) {
				html.append("<td><form method='post'>" + "<input hidden id='action' name='action' value='deleteuser'>"
						+ String.format("<input hidden id='id' name='id' value='%s'>", user.getId())
						+ "<button type='submit'>Delete User</button></form></td>");
			}

			html.append("</tr>");
		}

		html.append("</table>").append("</div>");

		return html.toString();
	}

	/**
	 * Generates the HTML form for creating new users.
	 */
	@Override
	public String buildCreateUser() {
		FormBuilder form = new FormBuilder();

		form.setTitle("Create User").addHiddenInput("action", "createUser")
				.addRequiredInput("text", "First Name", "fName").addRequiredInput("text", "Last Name", "lName")
				.addSelect("Role", "role",
						new String[][] { { "regular", "Regular" }, { "manager", "Manager" }, { "admin", "Admin" } })
				.addRequiredInput("text", "Email", "email").addRequiredInput("password", "Password", "password")
				.addRequiredInput("password", "Repeat Password", "repeatedpass");

		return "<div class='action-section'>" + form.createForm(false, false) + "</div>";

	}
}
