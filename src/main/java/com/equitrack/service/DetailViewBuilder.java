package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.User;

/**
 * Builds the HTML view for displaying detailed information about a specific
 * piece of equipment The view includes general equipment info, notes, status,
 * and actions based on the user's role
 */
public class DetailViewBuilder extends PageBuilder {
	/** The user currently accessing the detail view */
	private User user;
	/** The equipment whose details are being displayed */
	private Equipment equipment;

	/**
	 * Constructs a DetailViewBuilder with a specified user and equipment
	 *
	 * @param user      the user viewing the details
	 * @param equipment the equipment to display
	 */
	public DetailViewBuilder(User user, Equipment equipment) {
		this.user = user;
		this.equipment = equipment;
	}

	/**
	 * Builds the full HTML content of the equipment detail page Displays name, ID,
	 * image, location, notes, return date (if applicable), and admin actions
	 *
	 * @return the HTML string of the page
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		String status = equipment.isOperationalString();
		FormBuilder builder = new FormBuilder();

		html.append("<!DOCTYPE html>").append("<html lang=\"en\"><head>").append("<meta charset=\"UTF-8\">")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
				.append("<title>Equipment Details - Construction Equipment Tracker</title>")
				.append("<link rel=\"stylesheet\" href=\"css/style.css\">").append("</head><body>");

		html.append("<div class='header'><div class='header-content'>")
				.append("<a href='ListView' class='back-btn'>&larr; Back to List</a><h1>Equipment Details</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
				.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>")
				.append("<img src='" + equipment.getImagePath() + "' alt='" + equipment.getName())
				.append("' class='equipment-image-detail'>").append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>" + equipment.getName() + "</div>")
				.append("<div class='equipment-id'>ID: " + equipment.getId() + "</div>")
				.append("<span class='status-tag status-" + status + "'>" + status.toUpperCase() + "</span>")
				.append("</div></div></div>");

		html.append("<div class='detail-section'>").append("<div class='section-title'>Equipment Information</div>")
				.append("<div class='detail-grid'>")
				.append("<div class='detail-item'><div class='item-label'>Location</div><div class='item-value'>"
						+ equipment.getLocation() + "</div></div>");
		
		if (!equipment.isOperational() && equipment.getReturnDate() != null) {
			html.append("<div class='detail-item'><div class='item-label'>Return Date</div><div class='item-value'>")
					.append(equipment.getReturnDate() + "</div></div>");
		}
		html.append("</div>");
		
		if (equipment.getNotes() != null && !equipment.getNotes().trim().isEmpty()) {
			html.append("<div class='notes-section'>").append("<div class='notes-title'>NOTES</div>")
					.append("<div class='notes-content'>").append(equipment.getNotes()).append("</div>")
					.append("</div>");
		}

		if (user.getRole().equalsIgnoreCase("Admin")) {
			html.append("<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>");
			
			builder.setMethod("get").setAction("DetailView").removeDefaultSubmit()
			.addHiddenInput("id", equipment.getId())
			.addCustomButton("Edit Equipment", "EditEquipment?id=" + equipment.getId(), "action-btn btn-edit");
			
			if (equipment.isOperational()) 
				builder.addCustomSubmit("Check Out", "action", "checkout", "action-btn btn-checkout");
			else 
				builder.addCustomSubmit("Return", "action", "return", "action-btn btn-return");
			
			builder.addCustomSubmit("Delete", "action", "delete", "action-btn btn-delete");
			html.append(builder.createForm(false, false));
			
		} else if (equipment.isOperational()) 
			html.append("<h2>Contact Admin to request this equipment: (613) 111-1111</h2>");

		html.append("</div></div>");
		html.append("</body></html>");

		return html.toString();
	}
}