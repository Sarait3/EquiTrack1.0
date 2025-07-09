package com.equitrack.service;

import java.util.HashMap;

/**
 * Simplifies and standardizes the creation of html forms.
 */
public class FormBuilder {
	private Form form;
	private boolean hasAction, defaultMethod;

	/**
	 * Instantiates a new Form to be created when a FormBuilder
	 * Object is created.
	 */
	public FormBuilder() {
		form = new Form();
		hasAction = false;
		defaultMethod = true;
	}

	/**
	 * Adds a new input and its label to the form.
	 * @param type		The type of input to be generated, eg. "text", "date", etc.
	 * @param name		The name to be printed in the label.
	 * @param id		The id and internal name of the input.
	 * @return
	 */
	public FormBuilder addInput(String type, String name, String id) {
		form.addInput(String.format(
				"<label for='%s'>%s</label>"
						+ "<input type='%s' name='%s' id='%s' value=''>",
						id, name,
						type, id, id));

		return this;
	}

	/**
	 * Adds a new input and its label to the form.
	 * @param type		The type of input to be generated, eg. "text", "date", etc.
	 * @param name		The name to be printed in the label.
	 * @param id		The id and internal name of the input.
	 * @param value		The initial value to be placed inside the input.
	 * @return
	 */
	public FormBuilder addInput(String type, String name, String id, String value) {
		form.addInput(String.format(
				"<label for='%s'>%s</label>"
						+ "<input type='%s' name='%s' id='%s' value='%s'>",
						id, name,
						type, id, id, value));

		return this;
	}

	/**
	 * Adds a new input and its label to the form.
	 * @param type			The type of input to be generated, eg. "text", "date", etc.
	 * @param name			The name to be printed in the label.
	 * @param id			The id and internal name of the input.
	 * @param value			The initial value to be placed inside the input.
	 * @param isReadOnly	Whether or not the field should be editable.
	 * @return
	 */
	public FormBuilder addInput(String type, String name, String id, String value, boolean isReadOnly) {
		String readOnly = isReadOnly ? "readonly" : "";
		form.addInput(String.format(
				"<label for='%s'>%s</label>"
						+ "<input type='%s' %s name='%s' id='%s' value='%s'>",
						id, name,
						type, readOnly, id, id, value));

		return this;
	}

	/**
	 * Adds a new required input and its label to the form.
	 * @param type		The type of input to be generated, eg. "text", "date", etc.
	 * @param name		The name to be printed in the label.
	 * @param id		The id and internal name of the input.
	 * @return
	 */
	public FormBuilder addRequiredInput(String type, String name, String id) {
		form.addInput(String.format(
				"<label for='%s'>%s</label>"
						+ "<input type='%s' required name='%s' id='%s' value=''>",
						id, name,
						type, id, id));

		return this;
	}

	/**
	 * Adds a new required input and its label to the form.
	 * @param type		The type of input to be generated, eg. "text", "date", etc.
	 * @param name		The name to be printed in the label.
	 * @param id		The id and internal name of the input.
	 * @param value		The initial value to be placed inside the input.
	 * @return
	 */
	public FormBuilder addRequiredInput(String type, String name, String id, String value) {
		form.addInput(String.format(
				"<label for='%s'>%s</label>"
						+ "<input type='%s' required name='%s' id='%s' value='%s'>",
						id, name,
						type, id, id, value));

		return this;
	}

	/**
	 * Adds a new hidden input to the form.
	 * @param id		The internal name and id of the input.
	 * @param value		The value to be stored in the hidden input.
	 * @return
	 */
	public FormBuilder addHiddenInput(String id, String value) {
		form.addInput(String.format(
				"<input type='hidden' name='%s' id='%s' value='%s'>",
				id, id, value));

		return this;
	}
	
	/**
	 * Allows the user to add an input field of type "file" and 
	 * specify the type of file the can be accepted.
	 * @param name		The name of the field to be displayed in the label
	 * @param id		The internal name and id of the input
	 * @param fileType	The accepted filetype, eg. "image/*"
	 * @return
	 */
	public FormBuilder addFileInput(String name, String id, String fileType) {
		form.addInput(String.format("<label for='%s'>%s</label>"
				+ "<input type='file' name='%s' id='%s' accept='%s'", 
				id, name, id, id, fileType));
		
		return this;
	}

	/**
	 * Sets the action of the form, usually the current Servlet's
	 * declared URL pattern. Defaults to an empty String.
	 * @param formAction
	 * @return
	 */
	public FormBuilder setAction(String formAction) {
		form.setFormAction(String.format("action='%s'", formAction));
		this.hasAction = true;

		return this;
	}

	/**
	 * Sets the method of the form. Defaults to post.
	 * @param formMethod
	 * @return
	 */
	public FormBuilder setMethod(String formMethod) {
		form.setFormMethod(String.format("method='%s'", formMethod));
		this.defaultMethod = false;

		return this;
	}

	/**
	 * Sets the title of the page to be displayed in the browser tab
	 * and above the form.
	 * @param formTitle
	 * @return
	 */
	public FormBuilder setTitle(String formTitle) {
		form.setFormTitle(formTitle);

		return this;
	}

	/**
	 * Removes the submit button from the form.
	 * @return
	 */
	public FormBuilder removeSubmit() {
		form.setHasSubmit(false);

		return this;
	}

	/**
	 * Adds a reset button to the form.
	 * @return
	 */
	public FormBuilder addReset() {
		form.setHasReset(true);

		return this;
	}

	/**
	 * Adds an error message to the form under the submit/reset buttons.
	 * @param errorMessage
	 * @return
	 */
	public FormBuilder addError(String errorMessage) {
		form.setErrorMessage(String.format("<p>%s<p>", errorMessage));

		return this;
	}
	
	/**
	 * Adds a select and its options to the form.
	 * @param name		The name to be printed in the label
	 * @param id		The internal name and id of the select tag
	 * @param options	A HashMap representing the options in the select. format should be {value : text}
	 * @return
	 */
	public FormBuilder addSelect(String name, String id, String[][] options) {
		String html = String.format("<label for='%s'>%s</label>"
				+ "<select id='%s' name='%s'>", id, name, id, id);
		
		for (String[] option : options) {
			html = html + String.format("<option value='%s'>%s</option>", option[0], option[1]);
		}
		
		html = html + "</select>";
		
		form.addInput(html);
		
		return this;
	}
	
	/**
	 * Allows the user to enter custom html in the form.
	 * @param html	The custom html to enter.
	 * @return
	 */
	public FormBuilder addCustomLine(String html) {
		form.addInput(html);
		
		return this;
	}

	/**
	 * Generates the html using the parameters set by previous method calls.
	 * Also generates the rest of the page, including head section
	 * 
	 * @param generateHead		Whether or not to generate the head section of the page.
	 * @param isMultipart	Whether or not to include enctype='multipart/form-data'
	 * @return returns the html of the form as a String
	 */
	public String createForm(boolean generateHead, boolean isMultipart) {
		StringBuilder sb = new StringBuilder();
		String errorMessage = form.getErrorMessage() != null ? form.getErrorMessage() : "";
		String formTitle = form.getFormTitle();
		String formAction = this.hasAction ? form.getFormAction() : "";
		String formMethod = this.defaultMethod ? "method='post'" : form.getFormMethod();
		String multipart = isMultipart ? "enctype='multipart/form-data'" : "";
		
		if (generateHead) {
			sb.append("<!DOCTYPE html><html lang='en'><head>")
			.append("<meta charset='UTF-8>")
			.append("<meta name='viewport' content='width=device-width, initial-scale='1.0'>")
			.append(errorMessage != null ? "<style>p {color: red; text-align: center}</style>" : "")
			.append(String.format("<title>%s</title>", formTitle))
			.append("<link rel='stylesheet' href='css/style.css'>")
			.append("</head>")
			.append("<body>")
			.append("<div class='header'>")
			.append(String.format("<h1>%s<h1>", formTitle))
			.append("<div class='header-content'>")
			.append("</div></div>");
		}
		
		sb.append("<form class='container-detail edit-form'" 
		+ formAction 
		+ formMethod 
		+ multipart
		+ ">");

		for (String input : form.getInputs()) {
			sb.append(input);
		}

		sb.append("<div class='form-buttons'>")
		.append(form.getHasSubmit() ? "<button type='submit'>Submit</button>" : "")
		.append(form.getHasReset() ? "<button type='reset'>Reset</button>" : "")
		.append("</div></form>")
		.append(errorMessage);
		
		if (generateHead) {
			sb.append("</body></html>");
		}
		
		return sb.toString();
	}
}