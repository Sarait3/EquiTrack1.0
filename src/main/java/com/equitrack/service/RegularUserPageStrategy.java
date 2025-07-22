package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Role-based page strategy for regular users Provides limited access and
 * actions appropriate to regular users
 */
public class RegularUserPageStrategy implements PageRoleStrategy {

	/**
	 * Builds the sidebar navigation for regular users
	 * 
	 * @return HTML string for the sidebar
	 */
	@Override
	public String buildSidebar() {
		return "<input type='checkbox' id='sidebar-toggle' hidden>" + "<div class='sidebar'><nav><ul>"
				+ "<li><a href='ListView'>Equipment List</a></li>"
				+ "<li><a href='RequestsList'>My Checkout Requests</a></li>"
				+ "<li><a href='UserManagement'>User Management</a></li>" + "</ul></nav></div>";
	}

	/**
	 * Builds the checkout button if the equipment is operational
	 * 
	 * @param user      the current user
	 * @param equipment the equipment item
	 * @return HTML string with checkout form or empty if not operational
	 */
	@Override
	public String buildEquipmentActions(User user, Equipment equipment) {
		if (!equipment.isOperational())
			return "";

		return new StringBuilder().append(
				"<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
				.append("<form method='GET' action='CheckoutForm'>").append("<input type='hidden' name='id' value='")
				.append(equipment.getId()).append("'>")
				.append("<button type='submit' name='action' value='checkout' class='action-btn btn-checkout'>Request Check out</button>")
				.append("</form></div></div>").toString();
	}

	/**
	 * Regular users do not have permission to take request actions
	 * 
	 * @param user    the current user
	 * @param request the checkout request
	 * @return empty string
	 */
	@Override
	public String buildRequestActions(User user, Request request) {
		return "";
	}

	/**
	 * Builds forms for regular users to update their own email and password.
	 * 
	 * @return HTML string with account management forms
	 */
	@Override
	public String buildManageAccount() {
		FormBuilder changePass = new FormBuilder();
		FormBuilder changeEmail = new FormBuilder();
		StringBuilder html = new StringBuilder();

		changePass.setTitle("Change Password").addHiddenInput("action", "changepassword")
				.addRequiredInput("password", "Old Password", "oldPass")
				.addRequiredInput("password", "New Password", "newPass");

		changeEmail.setTitle("Change Email").addHiddenInput("action", "changeemail")
				.addRequiredInput("text", "New Email", "newmail").addRequiredInput("password", "Password", "password");

		html.append("<div class='action-section'>").append(changePass.createForm(false, false))
				.append(changeEmail.createForm(false, false)).append("</div>");

		return html.toString();
	}

	/**
	 * Regular users cannot view or manage other users
	 * 
	 * @return empty string
	 */
	@Override
	public String buildUserList() {
		return "";
	}

	/**
	 * Regular users cannot create new users
	 * 
	 * @return empty string
	 */
	@Override
	public String buildCreateUser() {
		return "";
	}
}
