package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public class RegularUserPageStrategy implements PageRoleStrategy{
	
	@Override 
	public String buildSidebar() {
		return "<input type='checkbox' id='sidebar-toggle' hidden>"
		+ "<div class='sidebar'><nav><ul>"
		+ "<li><a href='ListView'>Equipment List</a></li>"
		+ "<li><a href='RequestsList'>My Checkout Requests</a></li>"
		+ "<li><a href='UserManagement'>User Management</a></li>"
		+ "</ul></nav></div>";
	}
	
	@Override
	public String buildEquipmentActions(User user, Equipment equipment) {
		if (!equipment.isOperational()) return "";

		return new StringBuilder()
			.append("<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
			.append("<form method='GET' action='CheckoutForm'>")
			.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
			.append("<button type='submit' name='action' value='checkout' class='action-btn btn-checkout'>Request Check out</button>")
			.append("</form></div></div>")
			.toString();
	}
	
	@Override
	public String buildRequestActions(User user, Request request) {
		return "";
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
		return "";
	}

	@Override
	public String buildCreateUser() {
		return "";
	}



}
