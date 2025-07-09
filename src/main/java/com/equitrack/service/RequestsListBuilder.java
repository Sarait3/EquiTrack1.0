package com.equitrack.service;

import java.util.ArrayList;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.UserDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public class RequestsListBuilder extends PageBuilder {
	private User user;
	private ArrayList<Request> requestsList;
	private String searchInput;
	private String statusFilter;

	public RequestsListBuilder(User user, ArrayList<Request> requestsList, String searchInput, String statusFilter) {
		this.user = user;
		this.requestsList = requestsList;
		this.searchInput = searchInput;
		this.statusFilter = statusFilter;
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		EquipmentDao equipmentDao = new EquipmentDao();
		UserDao userDao = new UserDao();

		html.append("<!DOCTYPE html><html lang=\"en\"><head>")
				.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Requests List</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

		html.append("<input type='checkbox' id='sidebar-toggle' hidden>").append("<div class='sidebar'><nav><ul>")
				.append("<li><a href='ListView'>Equipment List</a></li>");
		if (user.getRole().equalsIgnoreCase("Regular")) {
			html.append("<li><a href='RequestsList'>My Checkout Requests</a></li>").append("</ul></nav></div>");
		} else {
			html.append("<li><a href='RequestsList'>Checkout Requests</a></li>").append("</ul></nav></div>");
		}

		html.append("<div class='header'><div class='header-content'>");

		html.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>");

		html.append(
				"<h1><a href='ListView' style='color: inherit; text-decoration: none;'>Checkout Requests List</a></h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
				.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		// Form
		SearchBuilder search = new SearchBuilder();
		
		search.setAction("RequestList");
		search.addFilter(new String[][]{{"", "All Status"}, {"pending", "Pending"}, {"approved", "Approved"}, {"declined", "Declined"}});
		html.append(search.createSearch());

		if ((searchInput != null && !searchInput.trim().isEmpty())
				|| (statusFilter != null && !statusFilter.trim().isEmpty())) {
			html.append("<a href='RequestsList' class='reset-btn'>View All</a>");
		}
		html.append("</form>");

		html.append("<div class='requests-list'><div class='requests-header'>")
				.append("<div>User Name</div><div>Equipment ID</div><div>Equipment Name</div><div>Location</div>")
				.append("<div>Checkout Date</div><div>Return Date</div><div>Status</div>")
				.append("</div><div id='requests'>");

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