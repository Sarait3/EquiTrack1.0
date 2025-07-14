package com.equitrack.service;

import java.util.ArrayList;
import com.equitrack.model.Equipment;
import com.equitrack.model.User;

public class ListViewBuilder extends PageBuilder {
	private User user;
	private ArrayList<Equipment> equipmentList;
	private String searchInput;
	private String statusFilter;
	private PageRoleStrategy roleStrategy;

	public ListViewBuilder(User user, ArrayList<Equipment> equipmentList, String searchInput, String statusFilter) {
		this.user = user;
		this.equipmentList = equipmentList;
		this.searchInput = searchInput;
		this.statusFilter = statusFilter;
		 if (user.getRole().equalsIgnoreCase("Regular")) {
		        this.roleStrategy = new RegularUserPageStrategy();
		    } else {
		        this.roleStrategy = new ManagerPageStrategy();
		    }
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();

		html.append("<!DOCTYPE html><html lang=\"en\"><head>")
			.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>")
			.append("<title>Equipment List</title>")
			.append("<link rel='stylesheet' href='css/style.css'>")
			.append("</head><body>");

		html.append(roleStrategy.buildSidebar());

		html.append("<div class='header'><div class='header-content'>")
			.append("<label for='sidebar-toggle' class='sidebar-button'>&#9776;</label>")
			.append("<h1><a href='ListView' style='color: inherit; text-decoration: none;'>EquiTrack List</a></h1>")
			.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
			.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
			.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container'>");
		html.append("<div class='search-bar'>")
			.append("<form class='search-form' method='GET' action='ListView'>")
			.append("<input type='text' name='searchInput' class='search-input' placeholder='Search equipment...' value='")
			.append(searchInput != null ? searchInput : "").append("'>");

		html.append("<select name='statusFilter' class='search-input'>")
			.append("<option value=''").append("".equals(statusFilter) ? " selected" : "").append(">All Status</option>")
			.append("<option value='operational'").append("operational".equals(statusFilter) ? " selected" : "").append(">Operational</option>")
			.append("<option value='out of service'").append("out of service".equals(statusFilter) ? " selected" : "").append(">Out Of Service</option>")
			.append("</select>");

		html.append("<button type='submit' class='search-btn'>Search</button>");

		if ((searchInput != null && !searchInput.trim().isEmpty())
				|| (statusFilter != null && !statusFilter.trim().isEmpty())) {
			html.append("<a href='ListView' class='reset-btn'>View All</a>");
		} else if (user.getRole().equalsIgnoreCase("Admin")) {
			html.append("<a href='AddEquipment' class='add-btn'>Add Equipment</a>");
		}

		html.append("</form></div>"); 
		
		html.append("<div class='equipment-list'><div class='equipment-header'>")
			.append("<div>Image</div><div>ID</div><div>Name</div><div>Location</div><div>Status</div>")
			.append("</div><div id='equipmentItems'>");

		if (equipmentList.isEmpty()) {
			html.append("<div style='color: #666; text-align: center; font-size: 1.2rem; padding: 1.5rem; "
					+ "margin-top: 2rem; font-style: italic;'>No results found.</div>");
		} else {
			for (Equipment eq : equipmentList) {
				String status = eq.isOperationalString();
				html.append("<a class='equipment-item' href='DetailView?id=").append(eq.getId()).append("'>")
					.append("<img class='equipment-image' src='").append(eq.getImagePath()).append("' alt='")
					.append(eq.getName()).append("'>")
					.append("<div class='equipment-id'>").append(eq.getId().substring(0, 8)).append("</div>")
					.append("<div class='equipment-name'>").append(eq.getName()).append("</div>")
					.append("<div class='equipment-location'>").append(eq.getLocation()).append("</div>")
					.append("<span class='status-tag status-").append(status.replaceAll(" ", "-")).append("'>")
					.append(status.toUpperCase()).append("</span>")
					.append("</a>");
			}
		}

		html.append("</div></div></div>"); 
		html.append("</body></html>");
		return html.toString();
	}
}
