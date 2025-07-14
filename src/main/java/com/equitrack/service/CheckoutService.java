package com.equitrack.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.RequestDao;
import com.equitrack.model.*;

/**
 * Generates the HTML page for checking out a new piece of equipment.
 * Includes generating the checkout form and performing the checkout process.
 */
public class CheckoutService extends PageBuilder {
	private User user;
	private String itemId;
	private EquipmentDao equipmentDao;
	private RequestDao requestDao;

	public CheckoutService(User user, String itemId) {
		this.user = user;
		this.itemId = itemId;
		equipmentDao = new EquipmentDao();
		requestDao = new RequestDao();
	}

	public String buildPage() {
		Equipment equipment = equipmentDao.getEquipment(this.itemId);

		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html><html lang='en'><head>")
			.append("<meta charset='UTF-8'>")
			.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
			.append("<title>Checkout Request</title>")
			.append("<link rel='stylesheet' href='css/style.css'>")
			.append("</head><body>");

		html.append("<input type='checkbox' id='sidebar-toggle' hidden>")
			.append("<div class='sidebar'><nav><ul>")
			.append("<li><a href='ListView'>Equipment List</a></li>");

		if (user.getRole().equalsIgnoreCase("Regular")) {
			html.append("<li><a href='RequestsList'>My Checkout Requests</a></li>");
		} else {
			html.append("<li><a href='RequestsList'>Checkout Requests</a></li>");
		}

		html.append("</ul></nav></div>")
			.append("<div class='header'><div class='header-content'>")
			.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
			.append("<a href='DetailView?id=").append(equipment.getId())
			.append("' class='back-btn'>&larr; Back to Equipment Details</a>")
			.append("<h1>Checkout Request</h1>")
			.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
			.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName()).append("</span>")
			.append("<a href='Logout' class='back-btn'>Logout</a>")
			.append("</div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
			.append("<div class='detail-header'><div class='equipment-info'>")
			.append("<img src='").append(equipment.getImagePath()).append("' alt='")
			.append(equipment.getName()).append("' class='equipment-image'>")
			.append("<div class='equipment-details'>")
			.append("<div class='equipment-title'>").append(equipment.getName()).append("</div>")
			.append("<div class='equipment-id'>ID: ").append(equipment.getId()).append("</div>")
			.append("</div></div></div>");

		// 🔔 Show upcoming approved requests
		Map<UUID, Request> upcomingRequests = requestDao.getUpcomingApprovedRequests(equipment.getId());
		if (!upcomingRequests.isEmpty()) {
			html.append("<div class='unavailable-box'>")
				.append("<div class='unavailable-title'>Unavailable Dates</div>")
				.append("<ul class='unavailable-list'>");
			for (Request r : upcomingRequests.values()) {
				html.append("<li>")
					.append(r.getCheckoutDate()).append(" to ").append(r.getReturnDate())
					.append("</li>");
			}
			html.append("</ul></div>");
		}

		FormBuilder builder = new FormBuilder();

		builder.setTitle("Checkout")
			.setAction("CheckoutForm")
			.setMethod("post")
			.addHiddenInput("itemId", equipment.getId())
			.addRequiredInput("date", "Checkout Date:", "checkoutDate")
			.addRequiredInput("date", "Return Date:", "returnDate")
			.addRequiredInput("text", "Location:", "location")
			.addRequiredInput("text", "Explain why you need this item:", "notes")
			.removeDefaultSubmit()
			.addCustomLine("<div class='form-buttons'><button type='submit'>Submit</button>"
					+ "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

		html.append(builder.createForm(false, false));
		html.append("</body></html>");

		return html.toString();
	}

	/**
	 * Handles checkout request creation and optional auto-approval.
	 * Prevents submission if requested dates conflict with existing approved requests.
	 */
	public boolean requestCheckout(String itemId, String userId, String location, String notes, LocalDate checkoutDate,
			LocalDate returnDate) {
		RequestDao dao = new RequestDao();
		if (dao.hasDateConflict(itemId, checkoutDate, returnDate)) {
			return false;
		}

		Request request = new Request(userId, itemId, location, notes, checkoutDate, returnDate);
		if (user.getRole().equalsIgnoreCase("Admin") || user.getRole().equalsIgnoreCase("Manager")) {
			request.approve();
		}
		requestDao.createRequest(request);
		return true;
	}
}
