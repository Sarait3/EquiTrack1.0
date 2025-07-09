package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.User;

/**
 * Builds the HTML page for editing equipment form Only users with the 'Admin'
 * role are allowed to access this page, If a non-admin user tries to access the
 * page, an "Access Denied" message is shown.
 *
 */
public class EditEquipmentBuilder extends PageBuilder {
	/** The current logged-in user */
	private User user;

	/** The equipment to be edited */
	private Equipment equipment;

	/**
	 * Constructs an EditEquipmentBuilder with the specified user and equipment
	 *
	 * @param user the user currently logged in
	 * @param equipment the equipment to be edited
	 */
	public EditEquipmentBuilder(User user, Equipment equipment) {
		this.user = user;
		this.equipment = equipment;
	}

	/**
	 * Builds and returns the HTML content for the edit equipment page
	 *
	 * @return the generated HTML page as a String
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		String status = equipment.isOperationalString();
		FormBuilder builder = new FormBuilder();

		if (!user.getRole().equalsIgnoreCase("Regular")) {
			html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
					.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
					.append("<title>Edit Equipment</title>").append("<link rel='stylesheet' href='css/style.css'>")
					.append("</head><body>");

			html.append("<input type='checkbox' id='sidebar-toggle' hidden>");

			html.append("<div class='sidebar'>").append("<nav><ul>")
					.append("<li><a href='ListView'>Equipment List</a></li>")
					.append("<li><a href='RequestsList'>Checkout Requests</a></li>").append("</ul></nav></div>");

			html.append("<div class='header'><div class='header-content'>");

			html.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>");

			html.append("<a href='DetailView?id=")
					.append(equipment.getId()).append("' class='back-btn'>&larr; Back to Equipment Details</a>")
					.append("<h1>Edit Equipment</h1>")
					.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
					.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
					.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

			html.append("<div class='container-detail'><div class='equipment-detail'>")
					.append("<div class='detail-header'><div class='equipment-info'>")
					.append("<img src='" + equipment.getImagePath() + "' alt='" + equipment.getName()
							+ "' class='equipment-image'>")
					.append("<div class='equipment-details'>")
					.append("<div class='equipment-title'>" + equipment.getName() + "</div>")
					.append("<div class='equipment-id'>ID: " + equipment.getId() + "</div>")
					.append("<span class='status-tag status-" + status + "'>" + status.toUpperCase() + "</span>")
					.append("</div></div></div>");

			builder.setAction("EditEquipment").addHiddenInput("id", equipment.getId())
					.addRequiredInput("text", "Name:", "name", equipment.getName())
					.addRequiredInput("text", "Location:", "location", equipment.getLocation())
					.addFileInput("Image:", "imageFile", "image/*")
					.addCustomLine(new StringBuilder().append("<label for='isOperational'>Status:</label>")
							.append("<select id='isOperational' name='isOperational'>").append("<option value='true' ")
							.append(equipment.isOperational() ? "selected" : "").append(">Operational</option>")
							.append("<option value='false' ").append(!equipment.isOperational() ? "selected" : "")
							.append(">Out Of Service</option>").append("</select>").toString())
					.addInput("textarea", "Notes:", "notes", equipment.getNotes() != null ? equipment.getNotes() : "")
					.removeSubmit()
					.addCustomLine("<div class='form-buttons'><button type='submit'>Save Changes</button>"
							+ "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

			html.append(builder.createForm(false, true));

			html.append("</body></html>");
		} else { 
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