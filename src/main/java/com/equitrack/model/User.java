package com.equitrack.model;

import java.util.UUID;

/**
 * This class represents a user in the app. A user has an ID, name, email,
 * password, and role (admin or regular user).
 */
public class User {
	String id, fName, lName, email, password, role;

	/**
	 * Creates a new User with all fields specified.
	 *
	 * @param id       the user ID
	 * @param role     the role of the user
	 * @param fName    the user's first name
	 * @param lName    the user's last name
	 * @param email    the user's email address
	 * @param password the user's password
	 */
	public User(String id, String role, String fName, String lName, String email, String password) {
		this.id = id;
		this.role = role;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.password = password;
	}

	/**
	 * Creates a user with all fields specified, but automatically generates a UUID
	 * 
	 * @param role     the role of the user
	 * @param fName    the user's first name
	 * @param lName    the user's last name
	 * @param email    the user's email
	 * @param password the user's password
	 */
	public User(String role, String fName, String lName, String email, String password) {
		this.id = UUID.randomUUID().toString();
		this.role = role;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.password = password;
	}

	/** Returns the user's ID */
	public String getId() {
		return id;
	}

	/** Sets the user's ID */
	public void setId(String id) {
		this.id = id;
	}

	/** Returns the user's role */
	public String getRole() {
		return role;
	}

	/** Sets the user's role */
	public void setRole(String role) {
		this.role = role;
	}

	/** Returns the user's first name */
	public String getFName() {
		return fName;
	}

	/** Sets the user's first name */
	public void setFName(String fName) {
		this.fName = fName;
	}

	/** Returns the user's last name */
	public String getLName() {
		return lName;
	}

	/** Sets the user's last name */
	public void setLName(String lName) {
		this.lName = lName;
	}

	/** Returns the user's email */
	public String getEmail() {
		return email;
	}

	/** Sets the user's email */
	public void setEmail(String email) {
		this.email = email;
	}

	/** Returns the user's password */
	public String getPassword() {
		return password;
	}

	/** Sets the user's password */

	public void setPassword(String password) {
		this.password = password;
	}
}
