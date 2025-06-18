package builders;

public class ConfirmationPageBuilder extends PageBuilder {
	private String message;

	public ConfirmationPageBuilder(String message) {
		this.message = message;
	}

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
