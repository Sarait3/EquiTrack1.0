package com.equitrack.service;

import java.time.LocalDate;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.RequestDao;
import com.equitrack.model.*;

/**
 * Generates the HTML page for checking out a new piece of equipment Includes
 * generating the checkout form and performing the checkout process.
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
		html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Checkout Request</title>").append("<link rel='stylesheet' href='css/style.css'>")
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

		html.append("<a href='DetailView?id=").append(equipment.getId())
				.append("' class='back-btn'>&larr; Back to Equipment Details</a>").append("<h1>Checkout Request</h1>")
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
				// add availability
				.append("</div></div></div>");

		FormBuilder builder = new FormBuilder();

		builder.setTitle("Checkout").setAction("CheckoutForm").setMethod("post")
				.addHiddenInput("itemId", equipment.getId()).addRequiredInput("date", "Checkout Date:", "checkoutDate")
				.addRequiredInput("date", "Return Date:", "returnDate")
				.addRequiredInput("text", "Location:", "location")
				.addRequiredInput("text", "Explain why you need this item:", "notes").removeSubmit()
				.addCustomLine("<div class='form-buttons'><button type='submit'>Submit</button>"
						+ "<a href='DetailView?id=" + equipment.getId() + "' class='back-btn'>Cancel</a></div>");

		html.append(builder.createForm(false, false));

		System.out.println(html.toString());

		return html.toString();
	}

	/**
	 * Performs the equipment checkout process by updating the equipment's
	 * availability status, logging the checkout details, and saving changes in the
	 * database.
	 *
	 * @param itemId            The ID of the equipment being checked out
	 * @param userId            The ID of the user checking out the equipment
	 * @param checkoutDate The date the equipment is checked out
	 * @param returnDate    The expected return date
	 */
	public boolean requestCheckout(String itemId, int userId, String location, String notes, LocalDate checkoutDate,
			LocalDate returnDate) {
		RequestDao dao = new RequestDao();
		if (dao.hasDateConflict(itemId, checkoutDate, returnDate)) 
			return false;
		
		else {
			Request request = new Request(userId, itemId, location, notes, checkoutDate, returnDate);
			if (user.getRole().equalsIgnoreCase("Admin") || user.getRole().equalsIgnoreCase("Manager"))
				request.approve();
			requestDao.createRequest(request);
			return true;
		}


	}
}