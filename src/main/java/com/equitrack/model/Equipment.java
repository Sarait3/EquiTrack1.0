package com.equitrack.model;

import java.util.UUID;

public class Equipment {
	private String id;
	private String name;
	private boolean isAvailable;
	private String location;
	private String imagePath;
	private String notes;

	public Equipment(String name, String imagePath, String notes) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isAvailable = true;
		this.imagePath = imagePath;
		this.notes = notes;
	}

	public Equipment(String name, boolean isAvailable, String location, String imagePath, String notes) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isAvailable = isAvailable;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
	}

	public Equipment(String id, String name, boolean isAvailable, String location, String imagePath, String notes) {
		this.id = id;
		this.name = name;
		this.isAvailable = isAvailable;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailbale(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setId(String id) {
		this.id = id;
	}
}