package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.User;

/**
 * Builds the HTML page that shows detailed information about a piece of
 * equipment. Includes the equipment's name, ID, image, location, notes, status,
 * and return date if needed. Also shows user info and action buttons based on
 * the user's role.
 */

public class DetailViewBuilder extends PageBuilder {

	/** The user currently accessing the detail view */
	private User user;

	/** The equipment whose details are being displayed */
	private Equipment equipment;

	/** Role-specific page strategy to control UI rendering */
	private PageRoleStrategy roleStrategy;

	/**
	 * Constructs a DetailViewBuilder with a specified user and equipment. The
	 * sidebar and actions shown are based on the user's role
	 *
	 * @param user      the user viewing the equipment details
	 * @param equipment the equipment to display
	 */
	public DetailViewBuilder(User user, Equipment equipment) {
		this.user = user;
		this.equipment = equipment;
		if (user.getRole().equalsIgnoreCase("Regular")) {
			this.roleStrategy = new RegularUserPageStrategy();
		} else {
			this.roleStrategy = new ManagerPageStrategy();
		}
	}

	/**
	 * Builds the full HTML content of the equipment detail page. Displays name, ID,
	 * image, location, operational status, return date (if not available), notes
	 * (if any), and role-specific action buttons
	 *
	 * @return the complete HTML string of the equipment detail view
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		String status = equipment.isOperationalString();

		html.append("<!DOCTYPE html>").append("<html lang=\"en\"><head>").append("<meta charset=\"UTF-8\">")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
				.append("<title>Equipment Details</title>").append("<link rel=\"stylesheet\" href=\"css/style.css\">")
				.append("</head><body>");

		html.append(roleStrategy.buildSidebar());

		html.append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<a href='ListView' class='back-btn'>&larr; Back to List</a>")
				.append("<h1>Equipment Details</h1>").append("<div class='user-info'>")
				.append("<img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName())
				.append("</span>").append("<a href='Logout' class='back-btn'>Logout</a>").append("</div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>").append("<img src='")
				.append(equipment.getImagePath()).append("' alt='").append(equipment.getName())
				.append("' class='equipment-image-detail'>").append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>").append(equipment.getName()).append("</div>")
				.append("<div class='equipment-id'>ID: ").append(equipment.getId()).append("</div>")
				.append("<span class='status-tag status-").append(status.replaceAll(" ", "-")).append("'>")
				.append(status.toUpperCase()).append("</span>").append("</div></div></div>");

		html.append("<div class='detail-section'>").append("<div class='section-title'>Equipment Information</div>")
				.append("<div class='detail-grid'>")
				.append("<div class='detail-item'><div class='item-label'>Location</div><div class='item-value'>")
				.append(equipment.getLocation()).append("</div></div>");

		if (!equipment.isOperational() && equipment.getReturnDate() != null) {
			html.append("<div class='detail-item'><div class='item-label'>Return Date</div><div class='item-value'>")
					.append(equipment.getReturnDate()).append("</div></div>");
		}

		html.append("</div>");

		if (equipment.getNotes() != null && !equipment.getNotes().trim().isEmpty()) {
			html.append("<div class='notes-section'>").append("<div class='notes-title'>NOTES</div>")
					.append("<div class='notes-content'>").append(equipment.getNotes()).append("</div></div>");
		}

		html.append(roleStrategy.buildEquipmentActions(user, equipment));

		html.append("</div></div>");
		html.append("</body></html>");

		return html.toString();
	}
}
