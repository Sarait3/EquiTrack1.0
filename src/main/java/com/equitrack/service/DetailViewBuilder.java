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

		html.append("<!DOCTYPE html>").append("<html lang=\"en\"><head>").append("<meta charset=\"UTF-8\">")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
				.append("<title>Equipment Details</title>").append("<link rel=\"stylesheet\" href=\"css/style.css\">")
				.append("</head><body>");

		html.append("<input type='checkbox' id='sidebar-toggle' hidden>");

		html.append("<input type='checkbox' id='sidebar-toggle' hidden>").append("<div class='sidebar'><nav><ul>")
				.append("<li><a href='ListView'>Equipment List</a></li>");
		if (user.getRole().equalsIgnoreCase("Regular")) {
			html.append("<li><a href='RequestsList'>My Checkout Requests</a></li>").append("</ul></nav></div>");
		} else {
			html.append("<li><a href='RequestsList'>Checkout Requests</a></li>").append("</ul></nav></div>");
		}

		html.append("<div class='header'><div class='header-content'>");

		if (!user.getRole().equalsIgnoreCase("Regular")) {
			html.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>");
		}

		html.append("<a href='ListView' class='back-btn'>&larr; Back to List</a><h1>Equipment Details</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
				.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>")
				.append("<img src='" + equipment.getImagePath() + "' alt='" + equipment.getName())
				.append("' class='equipment-image-detail'>").append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>" + equipment.getName() + "</div>")
				.append("<div class='equipment-id'>ID: " + equipment.getId() + "</div>")
				.append("<span class='status-tag status-" + status.replaceAll(" ", "-") + "'>" + status.toUpperCase()
						+ "</span>")
				.append("</div></div></div>");

		html.append("<div class='detail-section'>").append("<div class='section-title'>Equipment Information</div>")
				.append("<div class='detail-grid'>")
				.append("<div class='detail-item'><div class='item-label'>Location</div><div class='item-value'>"
						+ equipment.getLocation() + "</div></div>");
		if (!equipment.isOperational() && equipment.getReturnDate() != null)
			html.append("<div class='detail-item'><div class='item-label'>Return Date</div><div class='item-value'>")
					.append(equipment.getReturnDate() + "</div></div>");
		html.append("</div>");
		if (equipment.getNotes() != null && !equipment.getNotes().trim().isEmpty()) {
			html.append("<div class='notes-section'>").append("<div class='notes-title'>NOTES</div>")
					.append("<div class='notes-content'>").append(equipment.getNotes()).append("</div>")
					.append("</div>");
		}

		if (user.getRole().equalsIgnoreCase("Regular")) {
			if (equipment.isOperational()) {
				html.append(
						"<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
						.append("<form method='GET' action='CheckoutForm'>")
						.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
						.append("<button type='submit' name='action' value='checkout' class='action-btn btn-checkout'>Request Check out</button>")
						.append("</form></div></div></div>");
			}
		} else {
			html.append(
					"<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
					.append("<a href='EditEquipment?id=").append(equipment.getId())
					.append("' class='action-btn btn-edit'>Edit Equipment</a>");

			if (equipment.isOperational()) {
				html.append("<form method='GET' action='CheckoutForm' style='display:inline-block;'>")
						.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
						.append("<button type='submit' class='action-btn btn-checkout'>Check out</button>")
						.append("</form>");
			} else {
				html.append("<form method='GET' action='DetailView' style='display:inline-block;'>")
						.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
						.append("<input type='hidden' name='action' value='backInService'>")
						.append("<button type='submit' class='action-btn btn-return'>Back In Service</button>")
						.append("</form>");
			}
			html.append("<form method='GET' action='DetailView' style='display:inline-block;'>")
					.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
					.append("<input type='hidden' name='action' value='delete'>")
					.append("<button type='submit' class='action-btn btn-delete'>Delete Equipment</button>")
					.append("</form>");

			html.append("</div></div></div>");
		}

		html.append("</div></div>");
		html.append("</body></html>");

		return html.toString();
	}
}