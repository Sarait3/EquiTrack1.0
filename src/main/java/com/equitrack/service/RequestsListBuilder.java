package com.equitrack.service;

import java.util.ArrayList;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Builds the HTML page to display a list of equipment checkout requests.
 * 
 */
public class RequestsListBuilder extends PageBuilder {
	private User user;
	private ArrayList<Request> requestsList;
	private String searchInput;
	private String statusFilter;
	private PageRoleStrategy roleStrategy;

	/**
	 * Constructs a RequestsListBuilder for the given user and request list
	 * Initializes the appropriate role-based strategy
	 *
	 * @param user         the logged-in user
	 * @param requestsList the list of requests to display
	 * @param searchInput  the current search keyword, if any
	 * @param statusFilter the current status filter, if any
	 */
	public RequestsListBuilder(User user, ArrayList<Request> requestsList, String searchInput, String statusFilter) {
		this.user = user;
		this.requestsList = requestsList;
		this.searchInput = searchInput;
		this.statusFilter = statusFilter;

		if (user.getRole().equalsIgnoreCase("Regular")) {
			this.roleStrategy = new RegularUserPageStrategy();
		} else {
			this.roleStrategy = new ManagerPageStrategy();
		}
	}

	/**
	 * Builds the full HTML page for the request list
	 *
	 * @return HTML string representing the list of requests
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		EquipmentDao equipmentDao = new EquipmentDao();
		UserDao userDao = new UserDao();

		// Page head
		html.append("<!DOCTYPE html><html lang=\"en\"><head>")
				.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Requests List</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

		// Sidebar
		html.append(roleStrategy.buildSidebar());

		// Header
		html.append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<h1><a href='ListView' style='color: inherit; text-decoration: none;'>Checkout Requests List</a></h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName())
				.append("</span>").append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container'>");

		// Search bar
		html.append("<div class='search-bar'>").append("<form class='search-form' method='GET' action='RequestsList'>")
				.append("<input type='text' name='searchInput' class='search-input' placeholder='Search requests...' value='")
				.append(searchInput != null ? searchInput : "").append("'>");

		html.append("<select name='statusFilter' class='search-input'>").append("<option value=''")
				.append("".equals(statusFilter) ? " selected" : "").append(">All Status</option>")
				.append("<option value='pending'").append("pending".equals(statusFilter) ? " selected" : "")
				.append(">Pending</option>").append("<option value='approved'")
				.append("approved".equals(statusFilter) ? " selected" : "").append(">Approved</option>")
				.append("<option value='declined'").append("declined".equals(statusFilter) ? " selected" : "")
				.append(">Declined</option>").append("</select>");

		html.append("<button type='submit' class='search-btn'>Search</button>");

		if ((searchInput != null && !searchInput.trim().isEmpty())
				|| (statusFilter != null && !statusFilter.trim().isEmpty())) {
			html.append("<a href='RequestsList' class='reset-btn'>View All</a>");
		}

		html.append("</form></div>");

		// Request list table headers
		html.append("<div class='requests-list'><div class='requests-header'>")
				.append("<div>User Name</div><div>Equipment ID</div><div>Equipment Name</div><div>Location</div>")
				.append("<div>Checkout Date</div><div>Return Date</div><div>Status</div>")
				.append("</div><div id='requests'>");

		// Request items
		if (requestsList.isEmpty()) {
			html.append("<div style='color: #666; text-align: center; font-size: 1.2rem; padding: 1.5rem; "
					+ "margin-top: 2rem; font-style: italic;'>No results found.</div>");
		} else {
			for (Request request : requestsList) {
				String status = request.getStatus().toLowerCase();
				User reqUser = userDao.getUserById(request.getUserId());
				Equipment eq = equipmentDao.getEquipment(request.getEquipmentId());

				html.append("<a class='request-item' href='RequestDetail?id=").append(request.getId()).append("'>")
						.append("<div class='user-name'>").append(reqUser.getFName()).append(" ")
						.append(reqUser.getLName()).append("</div>").append("<div class='equipment-id'>")
						.append(eq.getId().substring(0, 8)).append("</div>").append("<div class='equipment-name'>")
						.append(eq.getName()).append("</div>").append("<div class='equipment-location'>")
						.append(request.getLocation()).append("</div>").append("<div class='checkout-date'>")
						.append(request.getCheckoutDate()).append("</div>").append("<div class='return-date'>")
						.append(request.getReturnDate()).append("</div>")
						.append("<div class='status-cell'><span class='status-tag status-").append(status).append("'>")
						.append(status.toUpperCase()).append("</span></div>").append("</a>");
			}
		}

		html.append("</div></div></div>");
		html.append("</body></html>");

		return html.toString();
	}
}
