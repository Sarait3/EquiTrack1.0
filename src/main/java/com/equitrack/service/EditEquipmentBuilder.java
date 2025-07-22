package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.User;

/**
 * Builds the HTML page for editing an existing equipment record. Displays
 * equipment details and a pre-filled form that allows authorized users to
 * update fields
 */
public class EditEquipmentBuilder extends PageBuilder {
	private User user;
	private Equipment equipment;

	/**
	 * Constructs the builder with the current user and the equipment to edit.
	 *
	 * @param user      the authenticated user accessing the page
	 * @param equipment the equipment object to be edited
	 */
	public EditEquipmentBuilder(User user, Equipment equipment) {
		this.user = user;
		this.equipment = equipment;
	}

	/**
	 * Builds the HTML form view for editing equipment details Includes pre-filled
	 * values for name, location, notes, and status
	 *
	 * @return a String of the full HTML page
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		String status = equipment.isOperationalString();
		FormBuilder builder = new FormBuilder();

		html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Edit Equipment</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

		html.append("<input type='checkbox' id='sidebar-toggle' hidden>");

		html.append("<div class='sidebar'><nav><ul>").append("<li><a href='ListView'>Equipment List</a></li>")
				.append("<li><a href='RequestsList'>Checkout Requests</a></li>").append("</ul></nav></div>");

		html.append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<a href='DetailView?id=").append(equipment.getId())
				.append("' class='back-btn'>&larr; Back to Equipment Details</a>").append("<h1>Edit Equipment</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName())
				.append("</span>").append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>").append("<img src='")
				.append(equipment.getImagePath()).append("' alt='").append(equipment.getName())
				.append("' class='equipment-image'>").append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>").append(equipment.getName()).append("</div>")
				.append("<div class='equipment-id'>ID: ").append(equipment.getId()).append("</div>")
				.append("<span class='status-tag status-").append(status).append("'>").append(status.toUpperCase())
				.append("</span>").append("</div></div></div>");

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
				.removeDefaultSubmit()
				.addCustomLine("<div class='form-buttons'><button type='submit'>Save Changes</button>"
						+ "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

		html.append(builder.createForm(false, true));
		html.append("</body></html>");

		return html.toString();
	}
}
