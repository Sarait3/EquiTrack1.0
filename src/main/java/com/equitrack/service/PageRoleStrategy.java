package com.equitrack.service;

import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Defines role-specific page elements for the EquiTrack web application.
 * Implemented by strategy classes for Manager and Regular users
 */
public interface PageRoleStrategy {

	/**
	 * Builds the HTML sidebar with navigation links based on user role
	 * 
	 * @return HTML string for the sidebar
	 */
	public String buildSidebar();

	/**
	 * Builds action buttons related to a specific equipment item
	 * 
	 * @param user      the current user
	 * @param equipment the equipment to act on
	 * @return HTML string with role-specific action buttons
	 */
	public String buildEquipmentActions(User user, Equipment equipment);

	/**
	 * Builds action buttons for approving or declining a request
	 * 
	 * @param user    the current user
	 * @param request the checkout request
	 * @return HTML string for request actions
	 */
	public String buildRequestActions(User user, Request request);

	/**
	 * Builds the form(s) for managing the logged-in user's account
	 * 
	 * @return HTML string for account management section
	 */
	public String buildManageAccount();

	/**
	 * Builds the user management table (if applicable)
	 * 
	 * @return HTML string for listing users
	 */
	public String buildUserList();

	/**
	 * Builds the form to create a new user (if applicable)
	 * 
	 * @return HTML string for the create user form
	 */
	public String buildCreateUser();
}
