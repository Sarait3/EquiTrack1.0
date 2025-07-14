package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public class RegularUserPageStrategy implements PageRoleStrategy{
	
	@Override 
	public String buildSidebar() {
		return "<input type='checkbox' id='sidebar-toggle' hidden>"
		+ "<div class='sidebar'><nav><ul>"
		+ "<li><a href='ListView'>Equipment List</a></li>"
		+ "<li><a href='RequestsList'>My Checkout Requests</a></li>"
		+ "</ul></nav></div>";
	}
	
	@Override
	public String buildEquipmentActions(User user, Equipment equipment) {
		if (!equipment.isOperational()) return "";

		return new StringBuilder()
			.append("<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
			.append("<form method='GET' action='CheckoutForm'>")
			.append("<input type='hidden' name='id' value='").append(equipment.getId()).append("'>")
			.append("<button type='submit' name='action' value='checkout' class='action-btn btn-checkout'>Request Check out</button>")
			.append("</form></div></div>")
			.toString();
	}
	
	@Override
	public String buildRequestActions(User user, Request request) {
		return "";
	}



}
