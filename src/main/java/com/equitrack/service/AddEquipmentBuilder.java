package com.equitrack.service;

import com.equitrack.model.User;

/**
 * Generates the HTML page for adding a new piece of equipment.
 * This page is only accessible to users with the "Admin" or "Manager" role.
 */
public class AddEquipmentBuilder extends PageBuilder {
	private User user;

	/**
	 * Creates a new AddEquipmentBuilder for a given user
	 *
	 * @param user the currently logged-in user
	 */
	public AddEquipmentBuilder(User user) {
		this.user = user;
	}

	/**
	 * Builds and returns the HTML content for the Add Equipment page
	 * 
	 * @return a complete HTML string for the add equipment form or an access denied
	 *         message
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		FormBuilder builder = new FormBuilder();
		
		if (user.getRole().equalsIgnoreCase("Admin")) {

			html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
					.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
					.append("<title>Add Equipment</title>").append("<link rel='stylesheet' href='css/style.css'>")
					.append("</head><body>");

			html.append("<div class='header'><div class='header-content'>")
					.append("<a href='ListView' class='back-btn'>&larr; Back to List</a><h1>Add New Equipment</h1>")
					.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
					.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
					.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");
			
			builder.setAction("AddEquipment")
			.addRequiredInput("text", "Name:", "name")
			.addRequiredInput("text", "Location:", "location")
			.addFileInput("Image:", "image", "image/*")
			.addSelect("Status:", "isAvailable", new String[][] {{"true", "Available"}, {"false", "Unavailable"}})
			.addInput("textarea", "Notes:", "notes");
			
			html.append(builder.createForm(false, true));
		} else {
			// Access denied page
			html.append("<!DOCTYPE html><html lang='en'><head>")
				.append("<meta charset='UTF-8'>")
				.append("<title>Access Denied</title>")
				.append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>")
				.append("<div class='container-detail'><h2>Access Denied</h2>")
				.append("<p>You do not have permission to view this page.</p>")
				.append("<a href='ListView' class='back-btn'>&larr; Back to List</a></div>")
				.append("</body></html>");
		}

		return html.toString();
	}
}