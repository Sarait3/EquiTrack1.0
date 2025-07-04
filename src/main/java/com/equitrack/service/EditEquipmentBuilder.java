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
	 * @param user      the user currently logged in
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
		String status = equipment.isAvailableString();

		if (user.getRole().equalsIgnoreCase("Admin")) {
			html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
					.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
					.append("<title>Edit Equipment</title>").append("<link rel='stylesheet' href='css/style.css'>")
					.append("</head><body>");

			html.append("<div class='header'><div class='header-content'>").append("<a href='DetailView?id=")
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

			html.append("<form class= 'edit-form' action='EditEquipment' method='POST' enctype='multipart/form-data'>")
					.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")

					.append("<label for='name'>Name:</label>")
					.append("<input type='text' id='name' name='name' value='").append(equipment.getName())
					.append("' required>")

					.append("<label for='location'>Location:</label>")
					.append("<input type='text' id='location' name='location' value='").append(equipment.getLocation())
					.append("' required>")

					.append("<label for='image'>Image:</label>")
					.append("<input type='file' id='imageFile' name='imageFile' accept='image/*'>")

					.append("<label for='isAvailable'>Status:</label>")
					.append("<select id='isAvailable' name='isAvailable'>").append("<option value='true' ")
					.append(equipment.isAvailable() ? "selected" : "").append(">Available</option>")
					.append("<option value='false' ").append(!equipment.isAvailable() ? "selected" : "")
					.append(">Unavailable</option>").append("</select>")

					.append("<label for='notes'>Notes:</label>").append("<textarea id='notes' name='notes'>")
					.append(equipment.getNotes() != null ? equipment.getNotes() : "").append("</textarea>")

					.append("<div class='form-buttons'><button type='submit'>Save Changes</button>")
					.append("<a href='DetailView?id=").append(equipment.getId())
					.append("' class='back-btn'>Cancel</a></div>").append("</form>").append("</div>");

			html.append("</div></div>");
			html.append("</body></html>");
		} else
			html.append("<!DOCTYPE html><html><head><title>Access Denied</title></head><body>")
					.append("<h2>Access Denied</h2>").append("</body></html>");
		return html.toString();
	}

}
