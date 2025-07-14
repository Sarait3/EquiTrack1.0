package com.equitrack.service.UserManagement;

import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;
import com.equitrack.service.FormBuilder;

public class AdminUserManagementService extends ManagerUserManagementService{

	public AdminUserManagementService(User user) {
		super(user);
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		
		html.append(page + userList() + createUser() + manageAccount());
		
		return html.toString();
	}
	
	@Override
	protected String userList() {
		UserDao dao = new UserDao();
		Map<UUID, User> users = dao.getAllUsers();
		
		html = new StringBuilder();
		
		html.append("<div style='text-align:center' class='action-section container-detail'>")
			.append("<h1>Manage Users</h1>")
			.append("<table>")
			.append("<th></th><th></th>");
		
		for(User user : users.values()) {
			html.append("<tr>")
				.append(String.format("<td>%s, %s</td>", user.getLName(), user.getFName()))
				.append(String.format("<td><a href='UserManagement?action=edituser&id=%s'>Edit User</a></td>", user.getId()))
				.append(String.format("<td><a href='UserManagement?action=deleteuser&id=%s'>Delete User</a></td>", user.getId()))
				.append("</tr>");
		}
		
		html.append("</table>")
			.append("</div>");
		
		
		return html.toString();
	}
	
	protected String createUser() {
		form = new FormBuilder();
		
		form.setTitle("Create User")
			.addRequiredInput("text", "First Name", "fName")
			.addRequiredInput("text", "Last Name", "lName")
			.addSelect("Role", "role", new String[][] {{"regular", "Regular"}, {"manager", "Manager"}, {"admin", "Admin"}})
			.addRequiredInput("text", "Email", "email")
			.addRequiredInput("password", "Password", "password")
			.addRequiredInput("password", "Repeat Password", "repeatedpass");
		
		
		return "<div class='action-section'>"
				+ form.createForm(false, false)
				+ "</div>";
		
	}

}
