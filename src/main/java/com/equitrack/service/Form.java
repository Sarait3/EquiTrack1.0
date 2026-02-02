package com.equitrack.service;

import java.util.ArrayList;

/**
 * Represents the information to render and submit an HTML form.
 *
 * Includes metadata (title, action, method), optional UI details (style,
 * error message), and ordered collections of input and button definitions.
 *
 */
public class Form {
    private String formTitle, formAction, formMethod, errorMessage, style;
    private ArrayList<String> inputs, buttons;

    /**
     * Creates a form with null metadata fields and empty input/button lists.
     * Fields initialized to null: formTitle, formAction, formMethod, errorMessage, style.
     * Collections initialized as empty and preserve insertion order.
     */
    public Form() {
        this.formTitle = null;
        this.formAction = null;
        this.formMethod = null;
        this.inputs = new ArrayList<>();
        this.buttons = new ArrayList<>();
    }

    /**
     * Title shown above the form
     *
     * @return the current form title
     */
    public String getFormTitle() {
        return formTitle;
    }

    /**
     * Sets the title shown above the form
     *
     * @param formTitle the title text, may be null to clear
     */
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    /**
     * Target action URL for form submission
     *
     * @return the action URL
     */
    public String getFormAction() {
        return formAction;
    }

    /**
     * Sets the target action URL for form submission
     *
     * @param formAction absolute or relative URL, may be null to clear
     */
    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    /**
     * HTTP method used on submit
     *
     * @return the HTTP method
     */
    public String getFormMethod() {
        return formMethod;
    }

    /**
     * Sets the HTTP method used on submit
     *
     * @param formMethod the HTTP method
     */
    public void setFormMethod(String formMethod) {
        this.formMethod = formMethod;
    }

    /**
     * Returns the live list of input definitions
     *
     * @return a mutable list of input definitions
     */
    public ArrayList<String> getInputs() {
        return inputs;
    }

    /**
     * Appends an input definition to the form
     *
     * @param input the input definition to add
     */
    public void addInput(String input) {
        this.inputs.add(input);
    }

    /**
     * Returns the live list of button definitions
     *
     * @return a mutable list of button definitions
     */
    public ArrayList<String> getButtons() {
        return buttons;
    }

    /**
     * Appends a button definition to the form
     *
     * @param button the button definition to add
     */
    public void addButton(String button) {
        this.buttons.add(button);
    }

    /**
     * Error message to display with the form
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message to display with the form
     *
     * @param errorMessage message text, may be null to clear
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Optional style hint for rendering the form
     *
     * @return the style string, or null if not set
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets an optional style hint for rendering the form
     *
     * @param style style string, may be null to clear
     */
    public void setStyle(String style) {
        this.style = style;
    }
}
