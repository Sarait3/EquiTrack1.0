package com.equitrack.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * This class represents a piece of equipment in the app. It stores details like
 * name, location, image path, operational status, notes, and a return date.
 */
public class Equipment {
	private String id, name, location, imagePath, notes;
	private boolean isOperational;
	private LocalDate returnDate;

	/**
	 * Creates a new operational equipment item with a generated ID
	 *
	 * @param name       the name of the equipment
	 * @param imagePath  the image path for the equipment
	 * @param notes      any additional notes
	 * @param returnDate the return date of the equipment
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
	 *
	 * @param name          the name of the equipment
	 * @param isOperational whether the equipment is operational
	 * @param location      the location of the equipment
	 * @param imagePath     the image path for the equipment
	 * @param notes         any additional notes
	 * @param returnDate    the return date of the equipment
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
	 * Creates a new equipment item with operability given as a string
	 *
	 * @param name          the name of the equipment
	 * @param isOperational "operational" or "out of service"
	 * @param location      the location of the equipment
	 * @param imagePath     the image path for the equipment
	 * @param notes         any additional notes
	 * @param returnDate    the return date of the equipment
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
	 *
	 * @param id            the unique ID of the equipment
	 * @param name          the name of the equipment
	 * @param isOperational operational status
	 * @param location      the location of the equipment
	 * @param imagePath     the image path
	 * @param notes         additional notes
	 * @param returnDate    the return date
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
	 * Creates an equipment item with a given ID and operability as a string.
	 *
	 * @param id            the unique ID of the equipment
	 * @param name          the name of the equipment
	 * @param isOperational "operational" or "out of service"
	 * @param location      the location of the equipment
	 * @param imagePath     the image path
	 * @param notes         additional notes
	 * @param returnDate    the return date
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

	/**
	 * Private constructor used by the Builder.
	 *
	 * @param builder the builder instance
	 */
	private Equipment(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.isOperational = builder.isOperational;
		this.location = builder.location;
		this.imagePath = builder.imagePath;
		this.notes = builder.notes;
		this.returnDate = builder.returnDate;
	}

	/** @return the equipment ID */
	public String getId() {
		return id;
	}

	/** @param id sets the equipment ID */
	public void setId(String id) {
		this.id = id;
	}

	/** @return the equipment name */
	public String getName() {
		return name;
	}

	/** @param name sets the equipment name */
	public void setName(String name) {
		this.name = name;
	}

	/** @return true if the equipment is operational */
	public boolean isOperational() {
		return this.isOperational;
	}

	/**
	 * @return "operational" if true, "out of service" otherwise
	 */
	public String isOperationalString() {
		return this.isOperational ? "operational" : "out of service";
	}

	/** @param isOperational sets the operational status */
	public void setOperational(boolean isOperational) {
		this.isOperational = isOperational;
	}

	/**
	 * Sets the operational status from a string.
	 * 
	 * @param isOperationalString "operational" or "out of service"
	 */
	public void setOperationalString(String isOperationalString) {
		this.isOperational = isOperationalString.equalsIgnoreCase("operational");
	}

	/** @return the equipment location */
	public String getLocation() {
		return location;
	}

	/** @param location sets the equipment location */
	public void setLocation(String location) {
		this.location = location;
	}

	/** @return additional notes */
	public String getNotes() {
		return notes;
	}

	/** @param notes sets additional notes */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/** @return the image path */
	public String getImagePath() {
		return imagePath;
	}

	/** @param imagePath sets the image path */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/** @return the return date */
	public LocalDate getReturnDate() {
		return returnDate;
	}

	/** @param returnDate sets the return date */
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	/**
	 * Builder class for Equipment. Allows fluent creation of immutable Equipment
	 * instances
	 */
	public static class Builder {
		private String id = UUID.randomUUID().toString();
		private String name;
		private boolean isOperational = true;
		private String location;
		private String imagePath;
		private String notes;
		private LocalDate returnDate;

		/** @param id sets the ID */
		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		/** @param name sets the name */
		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		/** @param isOperational sets the operational status */
		public Builder setOperational(boolean isOperational) {
			this.isOperational = isOperational;
			return this;
		}

		/** @param isOperationalString sets operational status from a string */
		public Builder setOperational(String isOperationalString) {
			this.isOperational = isOperationalString.equalsIgnoreCase("operational");
			return this;
		}

		/** @param location sets the location */
		public Builder setLocation(String location) {
			this.location = location;
			return this;
		}

		/** @param imagePath sets the image path */
		public Builder setImagePath(String imagePath) {
			this.imagePath = imagePath;
			return this;
		}

		/** @param notes sets the notes */
		public Builder setNotes(String notes) {
			this.notes = notes;
			return this;
		}

		/** @param returnDate sets the return date */
		public Builder setReturnDate(LocalDate returnDate) {
			this.returnDate = returnDate;
			return this;
		}

		/** @return the built Equipment instance */
		public Equipment build() {
			return new Equipment(this);
		}
	}
}
