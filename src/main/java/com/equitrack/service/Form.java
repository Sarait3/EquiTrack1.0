package com.equitrack.service;

import java.util.ArrayList;

/**
 * Represents the information that will be presented and requested
 * in an html form.
 */
public class Form {
	private String formTitle, formAction, formMethod, errorMessage;
	private ArrayList<String> inputs;
	private boolean hasSubmit, hasReset;
	
	/**
	 * Constructs a Form object with null fields and default values.
	 */
	public Form() {
		this.formTitle = null;
		this.formAction = null;
		this.formMethod = null;
		this.hasSubmit = true;
		this.hasReset = false;
		this.inputs = new ArrayList<>();
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

	public boolean getHasSubmit() {
		return hasSubmit;
	}

	public void setHasSubmit(boolean hasSubmit) {
		this.hasSubmit = hasSubmit;
	}

	public boolean getHasReset() {
		return hasReset;
	}

	public void setHasReset(boolean hasReset) {
		this.hasReset = hasReset;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
