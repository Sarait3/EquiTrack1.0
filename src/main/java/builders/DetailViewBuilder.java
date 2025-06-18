package builders;

import java.util.ArrayList;

import com.test.model.Equipment;
import com.test.model.User;

public class DetailViewBuilder extends PageBuilder {
	private User user;
	private Equipment equipment;

	public DetailViewBuilder(User user, Equipment equipment) {
		this.user = user;
		this.equipment = equipment;
	}

	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		String statusClass = equipment.isAvailable() ? "available" : "unavailable";
		String statusText = equipment.isAvailable() ? "AVAILABLE" : "UNAVAILABLE";

		html.append("<!DOCTYPE html>").append("<html lang=\"en\"><head>").append("<meta charset=\"UTF-8\">")
				.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
				.append("<title>Equipment Details - Construction Equipment Tracker</title>")
				.append("<link rel=\"stylesheet\" href=\"css/style.css\">").append("</head><body>");

		html.append("<div class='header'><div class='header-content'>")
				.append("<a href='ListView' class='back-btn'>&larr; Back to List</a><h1>Equipment Details</h1>")
				.append("<div class='user-info'><img src='images/user-icon.png' alt='User Icon' class='user-icon'>")
				.append("<span class='username'>" + user.getFName() + " " + user.getLName() + "</span>")
				.append("<a href='Logout' class='back-btn'>Logout</a></div></div></div>");

		html.append("<div class='container-detail'><div class='equipment-detail'>")
				.append("<div class='detail-header'><div class='equipment-info'>")
				.append("<img src='" + equipment.getImagePath() + "' alt='" + equipment.getName()
						+ "' class='equipment-image-detail'>")
				.append("<div class='equipment-details'>")
				.append("<div class='equipment-title'>" + equipment.getName() + "</div>")
				.append("<div class='equipment-id'>ID: " + equipment.getId() + "</div>")
				.append("<span class='status-tag status-" + statusClass + "'>" + statusText + "</span>")
				.append("</div></div></div>");

		html.append("<div class='detail-section'>").append("<div class='section-title'>Equipment Information</div>")
				.append("<div class='detail-grid'>")
				.append("<div class='detail-item'><div class='item-label'>Location</div><div class='item-value'>"
						+ equipment.getLocation() + "</div></div>")
				.append("<div class='detail-item'><div class='item-label'>Status</div><div class='item-value'>"
						+ statusText + "</div></div>")
				.append("</div>");
		if (equipment.getNotes() != null && !equipment.getNotes().trim().isEmpty()) {
			html.append("<div class='notes-section'>").append("<div class='notes-title'>NOTES</div>")
					.append("<div class='notes-content'>").append(equipment.getNotes()).append("</div>")
					.append("</div>");
		}

		if (user.getRole().equals("Admin")) {
			html.append(
					"<div class='actions-section'><div class='actions-title'>Actions</div><div class='action-buttons'>")
					.append("<form method='GET' action='DetailView'>").append("<input type='hidden' name='id' value='")
					.append(equipment.getId()).append("'>").append("<a href='EditEquipment?id=")
					.append(equipment.getId()).append("' class='action-btn btn-edit'>Edit Equipment</a>");
			if (equipment.isAvailable())
				html.append(
						"<button type='submit' name='action' value='checkout' class='action-btn btn-checkout'>Check out</button>");
			else
				html.append(
						"<button type='submit' name='action' value='return' class='action-btn btn-return'>Return</button>");
			html.append(
					"<button type='submit' name='action' value='delete' class='action-btn btn-delete'>Delete Equipment</button>")
					.append("</form></div></div></div>");
		} else if (equipment.isAvailable())
			html.append("<h2>Contact Admin to request this equipment: (613) 111-1111</h2>");
		html.append("</body></html>");

		return html.toString();
	}

}
