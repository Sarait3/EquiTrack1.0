package com.equitrack.service;

/**
 * Builds a simple HTML confirmation page displaying a success message. This
 * page is typically used after successful actions such as adding, editing, or
 * deleting equipment records
 */
public class ConfirmationPageBuilder extends PageBuilder {
	/** The message to be displayed on the confirmation page */
	private String message;

	/**
	 * Constructs a ConfirmationPageBuilder with the provided message
	 *
	 * @param message The success message to display to the user
	 */
	public ConfirmationPageBuilder(String message) {
		this.message = message;
	}

	/**
	 * Builds the HTML content for the confirmation page
	 *
	 * @return A complete HTML page as a string containing the confirmation message.
	 */
	@Override
	public String buildPage() {
		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html><html lang='en'><head>")
				.append("<meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>")
				.append("<title>Confirmation</title>").append("<link rel='stylesheet' href='css/style.css'>")
				.append("</head><body>").append("<div class='header'>").append("<div class='header-content'>")
				.append("<a href='ListView' class='back-btn'>&larr; Back to List</a>").append("<h1>Success!</h1>")
				.append("</div></div>").append("<div class='container'><div class='empty-state'>")
				.append("<h3 style='color:green;'>").append(message).append("</h3>")
				.append("</div></div></body></html>");
		return html.toString();
	}
}
