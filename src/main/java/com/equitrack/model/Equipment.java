package com.equitrack.model;

import java.time.LocalDate;
import java.util.UUID;

public class Equipment {
	private String id, name, location, imagePath, notes;
	private boolean isAvailable;
	private LocalDate returnDate;

	public Equipment(String name, String imagePath, String notes, LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isAvailable = true;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	public Equipment(String name, boolean isAvailable, String location, String imagePath, String notes, LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isAvailable = isAvailable;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	public Equipment(String name, String isAvailable, String location, String imagePath, String notes, LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
		setAvailableString(isAvailable);
	}

	public Equipment(String id, String name, boolean isAvailable, String location, String imagePath, String notes, LocalDate returnDate) {
		this.id = id;
		this.name = name;
		this.isAvailable = isAvailable;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	public Equipment(String id, String name, String isAvailable, String location, String imagePath, String notes, LocalDate returnDate) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
		setAvailableString(isAvailable);
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
		return this.isAvailable;
	}
	
	public String isAvailableString() {
		return this.isAvailable() ? "Available" : "Unavailable";
	}

	public void setAvailbale(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	public void setAvailableString(String isAvailableString) {
		this.isAvailable = isAvailableString.equalsIgnoreCase("available") ? true : false;
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

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}