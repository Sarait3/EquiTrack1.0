package com.equitrack.service.UserManagement;

import com.equitrack.model.User;
import com.equitrack.service.FormBuilder;

public class RegularUserManagementService extends UserManagementService{
	protected FormBuilder form;
	protected StringBuilder html;
	protected String page;

	public RegularUserManagementService(User user) {
		super(user);
		
		html = new StringBuilder();
		
		page = html.append("</ul></nav></div>")
				.append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<h1>User Management</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName()).append("</span>")
				.append("<a href='Logout' class='back-btn'>Logout</a>")
				.append("</div></div></div>").toString();
				
	}

	@Override
	public String buildPage() {
		html = new StringBuilder();
		form = new FormBuilder();
		page = "<li><a href='RequestsList'>My Checkout Requests</a></li>" + page;

		html.append(page + manageAccount());
		
		return html.toString();
	}
	
	protected String manageAccount() {
		form = new FormBuilder();
		
		return "<div class='action-section'>"
				+ form.setTitle("Manage Account").addCustomButton("Change Email", "UserManagement?action=changeemail", "search-btn")
					.addCustomButton("Change Password", "UserManagement?action=changepassword", "search-btn")
					.createForm(false, false)
				+ "</div>";
	}

}
