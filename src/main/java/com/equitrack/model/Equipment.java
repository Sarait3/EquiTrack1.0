package com.equitrack.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * This class represents a piece of equipment in the app. It stores basic
 * details like name, location, image, availability, notes, and return date.
 */
public class Equipment {
	private String id, name, location, imagePath, notes;
	private boolean isAvailable;
	private LocalDate returnDate;

	/**
	 * Creates a new available equipment item with a generated ID
	 */
	public Equipment(String name, String imagePath, String notes, LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isAvailable = true;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	/**
	 * Creates a new equipment item with all fields provided
	 */
	public Equipment(String name, boolean isAvailable, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isAvailable = isAvailable;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	/**
	 * Creates a new equipment item with availability given as a string.
	 */
	public Equipment(String name, String isAvailable, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
		setAvailableString(isAvailable);
	}

	/**
	 * Creates an equipment item with a given ID and boolean availability
	 */
	public Equipment(String id, String name, boolean isAvailable, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = id;
		this.name = name;
		this.isAvailable = isAvailable;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	/**
	 * Creates an equipment item with a given ID and availability as a string
	 */
	public Equipment(String id, String name, String isAvailable, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
		setAvailableString(isAvailable);
	}

	/** Returns the equipment ID */
	public String getId() {
		return id;
	}

	/** Sets the equipment ID */
	public void setId(String id) {
		this.id = id;
	}

	/** Returns the name */
	public String getName() {
		return name;
	}

	/** Sets the name */
	public void setName(String name) {
		this.name = name;
	}

	/** Returns true if the equipment is available, false otherwise */
	public boolean isAvailable() {
		return this.isAvailable;
	}

	/** Returns availability as a String: "Available" or "Unavailable" */
	public String isAvailableString() {
		return this.isAvailable() ? "available" : "unavailable";
	}

	/** Sets availability using a boolean */
	public void setAvailbale(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	/** Sets availability using a String: "Available" or "Unavailable" */
	public void setAvailableString(String isAvailableString) {
		this.isAvailable = isAvailableString.equalsIgnoreCase("available") ? true : false;
	}

	/** Returns the location of the equipment */
	public String getLocation() {
		return location;
	}

	/** Sets the location */
	public void setLocation(String location) {
		this.location = location;
	}

	/** Returns notes */
	public String getNotes() {
		return notes;
	}

	/** Sets notes */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/** Returns the image path */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/** Sets the image path */
	public String getImagePath() {
		return imagePath;
	}

	/** Returns the return date */
	public LocalDate getReturnDate() {
		return returnDate;
	}

	/** Sets the return date */
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}