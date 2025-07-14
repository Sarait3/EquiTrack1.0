package com.equitrack.service.UserManagement;

import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.UserDao;
import com.equitrack.model.User;

public class ManagerUserManagementService extends RegularUserManagementService {

	public ManagerUserManagementService(User user) {
		super(user);
		
		page = "<li><a href='RequestsList'>Checkout Requests</a></li>" +
			"<li><a href='RequestsList'>My Checkout Requests</a></li>" +
			page;
	}
	
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		
		html.append(page + userList() + manageAccount());
		
		return html.toString();
	}
	
	protected String userList() {
		UserDao dao = new UserDao();
		Map<UUID, User> users = dao.getAllUsers();
		
		html = new StringBuilder();
		
		html.append("<div style='text-align:center' class='action-section container-detail'>")
			.append("<h1>Manage Users</h1>")
			.append("<table>")
			.append("<th>Name</th><th>Actions</th>");
		
		for(User user : users.values()) {
			html.append("<tr>")
				.append(String.format("<td>%s, %s</td>", user.getLName(), user.getFName()))
				.append(String.format("<td><a href='UserManagement?action=edituser&id=%s'>Edit User</a></td>", user.getId()))
				.append("</tr>");
		}
		
		html.append("</table>")
			.append("</div>");
		
		
		return html.toString();
	}

}
