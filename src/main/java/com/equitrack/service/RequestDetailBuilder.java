package com.equitrack.service;

import com.equitrack.dao.RequestDao;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public class RequestDetailBuilder extends PageBuilder {
	private User user;
	private Request request;

	public RequestDetailBuilder(User user, Request request) {
		this.user = user;
		this.request = request;
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		RequestDao requestDao = new RequestDao();
		Equipment equipment = requestDao.getEquipmentForRequest(request.getId());
		User reqUser = requestDao.getUserForRequest(request.getId());
		String status = request.getStatus();

		html.append("<!DOCTYPE html>").append("<html lang=\"en\"><head>").append("<meta charset=\"UTF-8\">")
		.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
		.append("<title>Request Details</title>").append("<link rel=\"stylesheet\" href=\"css/style.css\">")
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

		html.append("<a href='RequestsList' class='back-btn'>&larr; Back to List</a><h1>Request Details</h1>")
		.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
		.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
		.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
		.append("<div class='detail-header'><div class='equipment-info'>")
		.append("<img src='" + equipment.getImagePath() + "' alt='" + equipment.getName())
		.append("' class='equipment-image-detail'>").append("<div class='equipment-details'> ")
		.append("<div class='equipment-title'>Request for: " + equipment.getName() + "</div>")
		.append("<div class='equipment-id'>Request ID: " + request.getId().substring(0, 8) + "</div>")
		.append("<div class='equipment-id'>Request Date: " + request.getRequestDate() + "</div>")
		.append("<span class='status-tag status-" + status.toLowerCase() + "'>" + status.toUpperCase()
		+ "</span>")
		.append("</div></div></div>");

		html.append("<div class='detail-section'>").append("<div class='section-title'>Request Information</div>")
		.append("<div class='detail-grid'>")
		.append("<div class='detail-item'><div class='item-label'>User</div><div class='item-value'>"
				+ reqUser.getFName() + " " + reqUser.getLName() + "</div></div>")
		.append("<div class='detail-item'><div class='item-label'>Location</div><div class='item-value'>"
				+ equipment.getLocation() + "</div></div>");
		html.append("<div class='detail-item'><div class='item-label'>Checkout Date</div><div class='item-value'>")
		.append(request.getCheckoutDate() + "</div></div>");
		html.append("<div class='detail-item'><div class='item-label'>Return Date</div><div class='item-value'>")
		.append(request.getReturnDate() + "</div></div>");
		html.append("</div>");
		if (request.getNotes() != null && !request.getNotes().trim().isEmpty()) {
			html.append("<div class='notes-section'>").append("<div class='notes-title'>NOTES</div>")
			.append("<div class='notes-content'>").append(request.getNotes()).append("</div>").append("</div>");
			
			if (!user.getRole().equalsIgnoreCase("Regular") && request.getStatus().equalsIgnoreCase("pending")) {				
				for (String s : new String[] {"Approve", "Decline"}) {
					FormBuilder builder = new FormBuilder();
					
					builder.setMethod("get").setAction("RequestDetail").setStyle("display:inline-block")
					.addHiddenInput("id", request.getId())
					.addHiddenInput("action", s.toLowerCase())
					.addCustomSubmit(s, null, null, "action-btn btn-return");
					
					html.append(builder.createForm(false, false));
				}
				
			}

			html.append("</div></div></div>");
		}

		html.append("</div></div>");
		html.append("</body></html>");

		return html.toString();
	}
}