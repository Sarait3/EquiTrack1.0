package com.equitrack.service;

import java.util.ArrayList;

/**
 * Represents the information that will be presented and requested
 * in an html form.
 */
public class Form {
	private String formTitle, formAction, formMethod, errorMessage, style;
	private ArrayList<String> inputs, buttons;
	
	/**
	 * Constructs a Form object with null fields and default values.
	 */
	public Form() {
		this.formTitle = null;
		this.formAction = null;
		this.formMethod = null;
		this.inputs = new ArrayList<>();
		this.buttons = new ArrayList<>();
	}

	public String getFormTitle() {
		return formTitle;
	}
	
	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public String getFormAction() {
		return formAction;
	}
	
	public void setFormAction(String formAction) {
		this.formAction = formAction;
	}

	public String getFormMethod() {
		return formMethod;
	}
	
	public void setFormMethod(String formMethod) {
		this.formMethod = formMethod;
	}

	public ArrayList<String> getInputs() {
		return inputs;
	}
	
	public void addInput(String input) {
		this.inputs.add(input);
	}
	
	public ArrayList<String> getButtons() {
		return buttons;
	}
	
	public void addButton(String button) {
		this.buttons.add(button);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
