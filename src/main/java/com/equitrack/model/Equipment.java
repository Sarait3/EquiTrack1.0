package com.equitrack.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * This class represents a piece of equipment in the app. It stores basic
 * details like name, location, image, operability, notes, and return date.
 */
public class Equipment {
	private String id, name, location, imagePath, notes;
	private boolean isOperational;
	private LocalDate returnDate;

	/**
	 * Creates a new operational equipment item with a generated ID
	 */
	public Equipment(String name, String imagePath, String notes, LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isOperational = true;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	/**
	 * Creates a new equipment item with all fields provided
	 */
	public Equipment(String name, boolean isOperational, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.isOperational = isOperational;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	/**
	 * Creates a new equipment item with operability given as a string.
	 */
	public Equipment(String name, String isOperational, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
		setOperationalString(isOperational);
	}

	/**
	 * Creates an equipment item with a given ID and boolean operability
	 */
	public Equipment(String id, String name, boolean isOperational, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = id;
		this.name = name;
		this.isOperational = isOperational;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
	}

	/**
	 * Creates an equipment item with a given ID and operability as a string
	 */
	public Equipment(String id, String name, String isOperational, String location, String imagePath, String notes,
			LocalDate returnDate) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.imagePath = imagePath;
		this.notes = notes;
		this.setReturnDate(returnDate);
		setOperationalString(isOperational);
	}
	
	private Equipment(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.isOperational = builder.isOperational;
		this.location = builder.location;
		this.imagePath = builder.imagePath;
		this.notes = builder.notes;
		this.returnDate = builder.returnDate;
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
	public boolean isOperational() {
		return this.isOperational;
	}

	/** Returns operability as a String: "Operational" or "Out Of Service" */
	public String isOperationalString() {
		return this.isOperational() ? "operational" : "out of service";
	}

	/** Sets operability using a boolean */
	public void setOperational(boolean isOperational) {
		this.isOperational = isOperational;
	}

	/** Sets operability using a String: "Operational" or "Out Of Service" */
	public void setOperationalString(String isOperationalString) {
		this.isOperational = isOperationalString.equalsIgnoreCase("operational") ? true : false;
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
	
	public static class Builder {
		private String id = UUID.randomUUID().toString();
		private String name;
		private boolean isOperational = true;
		private String location;
		private String imagePath;
		private String notes;
		private LocalDate returnDate;

		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setOperational(boolean isOperational) {
			this.isOperational = isOperational;
			return this;
		}

		public Builder setOperational(String isOperationalString) {
			this.isOperational = isOperationalString.equalsIgnoreCase("operational");
			return this;
		}

		public Builder setLocation(String location) {
			this.location = location;
			return this;
		}

		public Builder setImagePath(String imagePath) {
			this.imagePath = imagePath;
			return this;
		}

		public Builder setNotes(String notes) {
			this.notes = notes;
			return this;
		}

		public Builder setReturnDate(LocalDate returnDate) {
			this.returnDate = returnDate;
			return this;
		}

		public Equipment build() {
			return new Equipment(this);
		}
	}

}