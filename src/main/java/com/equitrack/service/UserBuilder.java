package com.equitrack.service;

import com.equitrack.model.User;

/**
 * Builder class for creating new or existing User objects
 * 
 * This class supports step-by-step construction of a User by setting properties
 * such as ID, name, role, email, and password
 */
public class UserBuilder {
	private User user;
	private String id, fName, lName, role, email, password;

	/**
	 * Creates a new UserBuilder with all fields initialized to null
	 */
	public UserBuilder() {
		this.id = null;
		this.role = null;
		this.fName = null;
		this.lName = null;
		this.email = null;
		this.password = null;
	}

	/** Sets the user ID (used for existing users). */
	public UserBuilder setId(String id) {
		this.id = id;
		return this;
	}

	/** Sets the user's first name */
	public UserBuilder setFName(String fName) {
		this.fName = fName;
		return this;
	}

	/** Sets the user's last name */
	public UserBuilder setLName(String lName) {
		this.lName = lName;
		return this;
	}

	/** Sets the user's role (converted to lowercase) */
	public UserBuilder setRole(String role) {
		this.role = role.toLowerCase();
		return this;
	}

	/** Sets the user's email (converted to lowercase). */
	public UserBuilder setEmail(String email) {
		this.email = email.toLowerCase();
		return this;
	}

	/** Sets the user's password. */
	public UserBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	/**
	 * Creates a new User object
	 * 
	 * @return the built User object, or null if any required field is missing
	 */
	public User createUser() {
		if (role == null || fName == null || lName == null || email == null || password == null) {
			return null;
		} else if (id != null) {
			return createExistingUser();
		} else {
			return createNewUser();
		}
	}

	/**
	 * Creates a User object with a system-assigned ID (for new users)
	 */
	private User createNewUser() {
		user = new User(role, fName, lName, email, password);
		return user;
	}

	/**
	 * Creates a User object with an existing ID (for updating users)
	 */
	private User createExistingUser() {
		user = new User(id, role, fName, lName, email, password);
		return user;
	}
}
