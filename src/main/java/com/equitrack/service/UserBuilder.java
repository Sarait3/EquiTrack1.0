package com.equitrack.service;

import java.util.UUID;

import com.equitrack.model.User;

public class UserBuilder {
	User user;
	String id, fName, lName, role, email, password;
	
	public UserBuilder() {
		this.id = null;
		this.role = null;
		this.fName = null;
		this.lName = null;
		this.email = null;
		this.password = null;
	}

	public UserBuilder setId(String id) {
		this.id = id;

		return this;
	}

	public UserBuilder setFName(String fName) {
		this.fName = fName;

		return this;
	}

	public UserBuilder setLName(String lName) {
		this.lName = lName;

		return this;
	}

	public UserBuilder setRole(String role) {
		this.role = role.toLowerCase();

		return this;
	}

	public UserBuilder setEmail(String email) {
		this.email = email.toLowerCase();

		return this;
	}

	public UserBuilder setPassword(String password) {
	        this.password = password;
	        return this;
	}


	public User createUser() {
		if (role == null || fName == null || lName == null || email == null || password == null) return null;
		else if (id != null) return createExistingUser();
		else return createNewUser();
	}

	private User createNewUser() {
		user = new User(UUID.randomUUID().toString(), role, fName, lName, email, password);

		return user;
	}

	private User createExistingUser() {
		user = new User(id, role, fName, lName, email, password);

		return user;
	}
}
