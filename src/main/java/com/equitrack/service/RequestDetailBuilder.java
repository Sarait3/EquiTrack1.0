package com.equitrack.service;

import com.equitrack.dao.RequestDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Builds the HTML page to show detailed information about a specific checkout
 * request
 *
 */
public class RequestDetailBuilder extends PageBuilder {
	private User user;
	private Request request;
	private PageRoleStrategy roleStrategy;

	/**
	 * Constructs a RequestDetailBuilder for the given user and request Uses a role
	 * strategy to show actions appropriate to the user role
	 * 
	 * @param user    the logged-in user
	 * @param request the request to show details for
	 */
	public RequestDetailBuilder(User user, Request request) {
		this.user = user;
		this.request = request;
		if (user.getRole().equalsIgnoreCase("Regular")) {
			this.roleStrategy = new RegularUserPageStrategy();
		} else {
			this.roleStrategy = new ManagerPageStrategy();
		}
	}

	/**
	 * Builds the full HTML page showing request and equipment details
	 * 
	 * @return HTML string for the request detail view
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		RequestDao requestDao = new RequestDao();

		Equipment equipment = requestDao.getEquipmentForRequest(request.getId());
		User reqUser = requestDao.getUserForRequest(request.getId());
		String status = request.getStatus();

		html.append("<!DOCTYPE html><html lang=\"en\"><head>").append("<meta charset=\"UTF-8\">")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
				.append("<title>Request Details</title>").append("<link rel=\"stylesheet\" href=\"css/style.css\">")
				.append("</head><body>");

		// Sidebar
		html.append(roleStrategy.buildSidebar());

		// Header
		html.append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<a href='RequestsList' class='back-btn'>&larr; Back to List</a><h1>Request Details</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName())
				.append("</span>").append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		// Equipment image and summary
		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>").append("<img src='")
				.append(equipment.getImagePath()).append("' alt='").append(equipment.getName())
				.append("' class='equipment-image-detail'>").append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>Request for: ").append(equipment.getName()).append("</div>")
				.append("<div class='equipment-id'>Request ID: ").append(request.getId().substring(0, 8))
				.append("</div>").append("<div class='equipment-id'>Request Date: ").append(request.getRequestDate())
				.append("</div>").append("<span class='status-tag status-").append(status.toLowerCase()).append("'>")
				.append(status.toUpperCase()).append("</span>").append("</div></div></div>");

		// Request details
		html.append("<div class='detail-section'>").append("<div class='section-title'>Request Information</div>")
				.append("<div class='detail-grid'>")
				.append("<div class='detail-item'><div class='item-label'>User</div><div class='item-value'>")
				.append(reqUser.getFName()).append(" ").append(reqUser.getLName()).append("</div></div>")
				.append("<div class='detail-item'><div class='item-label'>Location</div><div class='item-value'>")
				.append(equipment.getLocation()).append("</div></div>")
				.append("<div class='detail-item'><div class='item-label'>Checkout Date</div><div class='item-value'>")
				.append(request.getCheckoutDate()).append("</div></div>")
				.append("<div class='detail-item'><div class='item-label'>Return Date</div><div class='item-value'>")
				.append(request.getReturnDate()).append("</div></div>").append("</div>");

		// Optional notes
		if (request.getNotes() != null && !request.getNotes().trim().isEmpty()) {
			html.append("<div class='notes-section'>").append("<div class='notes-title'>NOTES</div>")
					.append("<div class='notes-content'>").append(request.getNotes()).append("</div>").append("</div>");
		}

		// Role-based actions (approve/decline if manager)
		html.append(roleStrategy.buildRequestActions(user, request));

		html.append("</div></div></div>");
		html.append("</body></html>");

		return html.toString();
	}
}
