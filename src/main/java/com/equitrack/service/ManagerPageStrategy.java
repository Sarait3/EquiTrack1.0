package com.equitrack.service;

import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public class ManagerPageStrategy implements PageRoleStrategy{
	
	@Override 
	public String buildSidebar() {
		return "<input type='checkbox' id='sidebar-toggle' hidden>"
		+ "<div class='sidebar'><nav><ul>"
		+ "<li><a href='ListView'>Equipment List</a></li>"
		+ "<li><a href='RequestsList'>Checkout Requests</a></li>"
		+ "<li><a href='UserManagement'>User Management</a></li>"
		+ "</ul></nav></div>";
	}
	
	@Override
	public String buildEquipmentActions(User user, Equipment equipment) {
		StringBuilder html = new StringBuilder();
		html.append("<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
			.append("<a href='EditEquipment?id=").append(equipment.getId())
			.append("' class='action-btn btn-edit'>Edit Equipment</a>");

		if (equipment.isOperational()) {
			html.append("<form method='GET' action='CheckoutForm' style='display:inline-block;'>")
				.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
				.append("<button type='submit' class='action-btn btn-checkout'>Check out</button>")
				.append("</form>");
		} else {
			html.append("<form method='GET' action='DetailView' style='display:inline-block;'>")
				.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
				.append("<input type='hidden' name='action' value='backInService'>")
				.append("<button type='submit' class='action-btn btn-return'>Back In Service</button>")
				.append("</form>");
		}

		html.append("<form method='GET' action='DetailView' style='display:inline-block;'>")
			.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
			.append("<input type='hidden' name='action' value='delete'>")
			.append("<button type='submit' class='action-btn btn-delete'>Delete Equipment</button>")
			.append("</form>");

		html.append("</div></div>");
		return html.toString();
	}
	
	@Override
	public String buildRequestActions(User user, Request request) {
		if (!request.getStatus().equalsIgnoreCase("pending")) {
			return "";
		}

		StringBuilder html = new StringBuilder();
		html.append("<form method='GET' action='RequestDetail' style='display:inline-block;'>")
			.append("<input type='hidden' name='id' value='").append(request.getId()).append("'>")
			.append("<input type='hidden' name='action' value='approve'>")
			.append("<button type='submit' class='action-btn btn-return'>Approve</button>")
			.append("</form>");

		html.append("<form method='GET' action='RequestDetail' style='display:inline-block;'>")
			.append("<input type='hidden' name='id' value='").append(request.getId()).append("'>")
			.append("<input type='hidden' name='action' value='decline'>")
			.append("<button type='submit' class='action-btn btn-delete'>Decline</button>")
			.append("</form>");

		return html.toString();
	}
	
	@Override
	public String buildManageAccount() {
		FormBuilder changePass = new FormBuilder();
		FormBuilder changeEmail = new FormBuilder();
		StringBuilder html = new StringBuilder();
		
		changePass.setTitle("Change Password").addHiddenInput("action", "changepassword")
			.addRequiredInput("password", "Old Password", "oldPass")
			.addRequiredInput("password", "New Password", "newPass");
		
		changeEmail.setTitle("Change Email").addHiddenInput("action", "changeemail")
			.addRequiredInput("text", "New Email", "newmail")
			.addRequiredInput("password", "Password", "password");

		html.append("<div class='action-section'>")
			.append(changePass.createForm(false, false))
			.append(changeEmail.createForm(false, false))
			.append("</div>");
		
		return html.toString();
	}
	
	@Override
	public String buildUserList() {
		UserDao dao = new UserDao();
		Map<UUID, User> users = dao.getAllUsers();
		
		StringBuilder html = new StringBuilder();
		
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

	@Override
	public String buildCreateUser() {
		return "";
	}



}
