package com.equitrack.service;

import com.equitrack.model.User;

/**
 * Generates the HTML page for adding a new piece of equipment.
 * This page is only accessible to users with the "Admin" or "Manager" role.
 */
public class AddEquipmentBuilder extends PageBuilder {
	private User user;

	public AddEquipmentBuilder(User user) {
		this.user = user;
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();

		if (!user.getRole().equalsIgnoreCase("Regular")) {

			html.append("<!DOCTYPE html><html lang='en'><head>")
				.append("<meta charset='UTF-8'>")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Add Equipment</title>")
				.append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

			html.append("<input type='checkbox' id='sidebar-toggle' hidden>");

			html.append("<div class='sidebar'><nav><ul>")
				.append("<li><a href='ListView'>Equipment List</a></li>")
				.append("<li><a href='RequestsList'>Checkout Requests</a></li>")
				.append("</ul></nav></div>");

			html.append("<div class='header'><div class='header-content'>");

			html.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>");

			html.append("<h1>EquiTrack List</h1>")
				.append("<div class='user-info'>")
				.append("<img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName()).append("</span>")
				.append("<a href='Logout' class='logout-btn'>Logout</a>")
				.append("</div></div></div>"); 

			html.append("<form class='container-detail edit-form' action='AddEquipment' method='POST' enctype='multipart/form-data'>")
				.append("<label for='name'>Name:</label>")
				.append("<input type='text' id='name' name='name' required>")

				.append("<label for='location'>Location:</label>")
				.append("<input type='text' id='location' name='location' required>")

				.append("<label for='image'>Image:</label>")
				.append("<input type='file' id='imageFile' name='imageFile' accept='image/*'>")

				.append("<label for='isOperational'>Status:</label>")
				.append("<select id='isOperational' name='isOperational'>")
				.append("<option value='true'>Operational</option>")
				.append("<option value='false'>Out Of Service</option>")
				.append("</select>")

				.append("<label for='notes'>Notes:</label>")
				.append("<textarea id='notes' name='notes'></textarea>")

				.append("<div class='form-buttons'>")
				.append("<button type='submit'>Add Equipment</button>")
				.append("<a href='ListView' class='back-btn'>Cancel</a>")
				.append("</div></form>");

			html.append("</body></html>");

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