package com.equitrack.service;

/**
 * A utility class that builds a simple HTML page indicating the user does not
 * have permission to access the requested resource. This is typically used to
 * enforce role-based access control
 */
public class AccessDeniedBuilder extends PageBuilder {

	/**
	 * Builds an HTML page with an "Access Denied" message and a link to return to
	 * the list view
	 *
	 * @return a String containing the complete HTML markup
	 */
	public String buildPage() {
		StringBuilder html = new StringBuilder();

		html.append("<!DOCTYPE html><html lang='en'><head>").append("<meta charset='UTF-8'>")
				.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Access Denied</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>").append("<div class='header'><div class='header-content'>")
				.append("<h1>Access Denied</h1>").append("</div></div>")
				.append("<div class='container'><div class='empty-state'>")
				.append("<h3 style='color:red;'>You do not have permission to view this page.</h3>")
				.append("<a href='ListView' class='back-btn'>&larr; Back to List</a>").append("</div></div>")
				.append("</body></html>");

		return html.toString();
	}
}
