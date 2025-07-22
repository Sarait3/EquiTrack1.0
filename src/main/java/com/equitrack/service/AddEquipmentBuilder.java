package com.equitrack.service;

import com.equitrack.model.User;

/**
 * Generates the HTML page for adding a new piece of equipment. This page is
 * only accessible to users with the "Admin" or "Manager" role. It includes a
 * sidebar, user info in the header, and a form for submitting equipment
 * details.
 */
public class AddEquipmentBuilder extends PageBuilder {
	private User user;

	/**
	 * Constructs the builder with the given user information to personalize the
	 * header.
	 *
	 * @param user the authenticated user accessing the add equipment page
	 */
	public AddEquipmentBuilder(User user) {
		this.user = user;
	}

	/**
	 * Builds and returns the HTML page for adding new equipment. Includes a form
	 * with fields for name, location, image upload, status, and notes.
	 *
	 * @return a String containing the full HTML markup
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();

		FormBuilder builder = new FormBuilder();

		html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Add Equipment</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

		html.append("<input type='checkbox' id='sidebar-toggle' hidden>");

		html.append("<div class='sidebar'><nav><ul>").append("<li><a href='ListView'>Equipment List</a></li>")
				.append("<li><a href='RequestsList'>Checkout Requests</a></li>").append("</ul></nav></div>");

		html.append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<h1>Add New Equipment</h1>").append("<div class='user-info'>")
				.append("<img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName())
				.append("</span>").append("<a href='Logout' class='logout-btn'>Logout</a>")
				.append("</div></div></div>");

		builder.setAction("AddEquipment").addRequiredInput("text", "Name:", "name")
				.addRequiredInput("text", "Location:", "location").addFileInput("Image:", "imageFile", "image/*")
				.addSelect("Status:", "isOperational",
						new String[][] { { "true", "Operational" }, { "false", "Out Of Service" } })
				.addInput("textarea", "Notes:", "notes");

		html.append(builder.createForm(false, true));

		html.append("</body></html>");

		return html.toString();
	}
}
