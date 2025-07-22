package com.equitrack.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.RequestDao;
import com.equitrack.model.*;

/**
 * Service class responsible for generating the HTML page for submitting a
 * checkout request and handling the logic for creating and approving equipment
 * checkout requests
 */
public class CheckoutService extends PageBuilder {
	private User user;
	private String itemId;
	private EquipmentDao equipmentDao;
	private RequestDao requestDao;
	private PageRoleStrategy roleStrategy;

	/**
	 * Constructs the CheckoutService with a given user and equipment item ID The
	 * role of the user determines the sidebar rendering strategy
	 *
	 * @param user   the logged-in user
	 * @param itemId the ID of the equipment item to be checked out
	 */
	public CheckoutService(User user, String itemId) {
		this.user = user;
		this.itemId = itemId;
		this.equipmentDao = new EquipmentDao();
		this.requestDao = new RequestDao();
		if (user.getRole().equalsIgnoreCase("Regular")) {
			this.roleStrategy = new RegularUserPageStrategy();
		} else {
			this.roleStrategy = new ManagerPageStrategy();
		}
	}

	/**
	 * Builds and returns the HTML page allowing the user to submit a checkout
	 * request Includes a display of upcoming approved bookings for the equipment, a
	 * form for entering request details, and role-based layout features like
	 * sidebars
	 *
	 * @return a String containing the full HTML content for the checkout page
	 */
	public String buildPage() {
		Equipment equipment = equipmentDao.getEquipment(this.itemId);

		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Checkout Request</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

		html.append(roleStrategy.buildSidebar());

		html.append("</ul></nav></div>").append("<div class='header'><div class='header-content'>")
				.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
				.append("<a href='DetailView?id=").append(equipment.getId())
				.append("' class='back-btn'>&larr; Back to Equipment Details</a>").append("<h1>Checkout Request</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>").append(user.getFName()).append(" ").append(user.getLName())
				.append("</span>").append("<a href='Logout' class='back-btn'>Logout</a>").append("</div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>").append("<img src='")
				.append(equipment.getImagePath()).append("' alt='").append(equipment.getName())
				.append("' class='equipment-image'>").append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>").append(equipment.getName()).append("</div>")
				.append("<div class='equipment-id'>ID: ").append(equipment.getId()).append("</div>")
				.append("</div></div></div>");

		// Show upcoming approved requests
		Map<UUID, Request> upcomingRequests = requestDao.getUpcomingApprovedRequests(equipment.getId());
		if (!upcomingRequests.isEmpty()) {
			html.append("<div class='unavailable-box'>")
					.append("<div class='unavailable-title'>Unavailable Dates</div>")
					.append("<ul class='unavailable-list'>");
			for (Request r : upcomingRequests.values()) {
				html.append("<li>").append(r.getCheckoutDate()).append(" to ").append(r.getReturnDate())
						.append("</li>");
			}
			html.append("</ul></div>");
		}

		FormBuilder builder = new FormBuilder();

		builder.setTitle("Checkout").setAction("CheckoutForm").setMethod("post")
				.addHiddenInput("itemId", equipment.getId()).addRequiredInput("date", "Checkout Date:", "checkoutDate")
				.addRequiredInput("date", "Return Date:", "returnDate")
				.addRequiredInput("text", "Location:", "location")
				.addRequiredInput("text", "Explain why you need this item:", "notes").removeDefaultSubmit()
				.addCustomLine("<div class='form-buttons'><button type='submit'>Submit</button>"
						+ "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

		html.append(builder.createForm(false, false));
		html.append("</body></html>");

		return html.toString();
	}

	/**
	 * Processes a checkout request. If the user has a manager or admin role, the
	 * request is auto-approved If the requested date range conflicts with existing
	 * approved requests, the operation fails
	 *
	 * @param itemId       the equipment ID being requested
	 * @param userId       the ID of the user submitting the request
	 * @param location     where the equipment will be used
	 * @param notes        purpose or context of the request
	 * @param checkoutDate the desired checkout date
	 * @param returnDate   the desired return date
	 * @return true if the request was successfully created (and optionally
	 *         approved); false if there was a conflict
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
