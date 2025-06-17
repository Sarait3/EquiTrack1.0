package builders;

import java.util.ArrayList;
import com.test.model.Equipment;
import com.test.model.User;

public class ListViewBuilder extends PageBuilder {
	private User user;
	private ArrayList<Equipment> equipmentList;
	private String searchInput;
	private String statusFilter;

	public ListViewBuilder(User user, ArrayList<Equipment> equipmentList, String searchInput, String statusFilter) {
		this.user = user;
		this.equipmentList = equipmentList;
		this.searchInput = searchInput;
		this.statusFilter = statusFilter;
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();

		html.append("<!DOCTYPE html><html lang=\"en\"><head>")
				.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Equipment List</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>");

		html.append(
				"<div class='header'><div class='header-content'><h1><a href='ListView' style='color: inherit; text-decoration: none;'>EquiTrack List</a></h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
				.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container'>").append("<form class= 'search-form' method='GET' action='ListView'>")
				.append("<input type='text' name='searchInput' class='search-input' placeholder='Search equipment...'>")
				.append("<select name='statusFilter' class='filter-select'>")
				.append("<option value=''>All Status</option>").append("<option value='available'>Available</option>")
				.append("<option value='unavailable'>Unavailable</option>").append("</select>")
				.append("<button type='submit' class='search-btn'>Search</button>");
		if ((searchInput != null && !searchInput.trim().isEmpty())
				|| statusFilter != null && !statusFilter.trim().isEmpty()) {
			html.append("<a href='ListView' class='reset-btn'>View All</a>");
		} else {
			if (user.getRole().equals("Admin")) {
				html.append("<a href='AddEquipment' class='add-btn'>Add Equipment</a>");
			}
		}
		html.append("</form>");

		html.append("<div class='equipment-list'><div class='equipment-header'>")
				.append("<div>Image</div><div>ID</div><div>Name</div><div>Location</div><div>Status</div>")
				.append("</div><div id='equipmentItems'>");

		for (Equipment eq : equipmentList) {
			String status = eq.isAvailable() ? "available" : "unavailable";
			html.append("<a class='equipment-item' href='DetailView?id=").append(eq.getId()).append("'>")
					.append("<img class='equipment-image' src='").append(eq.getImagePath()).append("' alt='")
					.append(eq.getName()).append("'>").append("<div class='equipment-id'>")
					.append(eq.getId().substring(0, 8)).append("</div>").append("<div class='equipment-name'>")
					.append(eq.getName()).append("</div>").append("<div class='equipment-location'>")
					.append(eq.getLocation()).append("</div>").append("<span class='status-tag status-").append(status)
					.append("'>").append(status.toUpperCase()).append("</span>").append("</a>");
		}

		html.append("</div></div></div>");
		html.append("</body></html>");
		return html.toString();
	}
}
