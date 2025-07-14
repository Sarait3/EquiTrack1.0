package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

public class ManagerPageStrategy implements PageRoleStrategy{
	
	@Override 
	public String buildSidebar() {
		return "<input type='checkbox' id='sidebar-toggle' hidden>"
		+ "<div class='sidebar'><nav><ul>"
		+ "<li><a href='ListView'>Equipment List</a></li>"
		+ "<li><a href='RequestsList'>Checkout Requests</a></li>"
		+ "</ul></nav></div>";
	}
	
	@Override
	public String buildEquipmentActions(User user, Equipment equipment) {
		StringBuilder html = new StringBuilder();
		html.append("<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
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

		html.append("</div></div>");
		return html.toString();
	}
	
	@Override
	public String buildRequestActions(User user, Request request) {
		if (!request.getStatus().equalsIgnoreCase("pending")) {
			return "";
		}

		StringBuilder html = new StringBuilder();
		html.append("<form method='GET' action='RequestDetail' style='display:inline-block;'>")
			.append("<input type='hidden' name='id' value='").append(request.getId()).append("'>")
			.append("<input type='hidden' name='action' value='approve'>")
			.append("<button type='submit' class='action-btn btn-return'>Approve</button>")
			.append("</form>");

		html.append("<form method='GET' action='RequestDetail' style='display:inline-block;'>")
			.append("<input type='hidden' name='id' value='").append(request.getId()).append("'>")
			.append("<input type='hidden' name='action' value='decline'>")
			.append("<button type='submit' class='action-btn btn-delete'>Decline</button>")
			.append("</form>");

		return html.toString();
	}



}
