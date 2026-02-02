package com.equitrack.model;

import java.time.LocalDate;
import java.util.UUID;

import com.equitrack.dao.RequestDao;

/**
 * Represents a request to check out a piece of equipment Contains information
 * about the requester, requested item, schedule, location, notes, and status
 */
public class Request {
	private String userId, id, equipmentId, location, notes, status;
	private LocalDate requestDate, checkoutDate, returnDate;

	/**
	 * Constructs a Request object with a provided ID and all fields specified
	 *
	 * @param id           the unique request ID
	 * @param userId       the user ID of the person making the request
	 * @param equipmentId  the ID of the equipment being requested
	 * @param status       the current status of the request (e.g., PENDING,
	 *                     APPROVED)
	 * @param location     the location where the equipment will be used
	 * @param notes        any additional notes about the request
	 * @param requestDate  the date the request was made
	 * @param checkoutDate the desired checkout date
	 * @param returnDate   the expected return date
	 */
	public Request(String id, String userId, String equipmentId, String status, String location, String notes,
			LocalDate requestDate, LocalDate checkoutDate, LocalDate returnDate) {
		this.id = id;
		this.userId = userId;
		this.equipmentId = equipmentId;
		this.status = status;
		this.location = location;
		this.notes = notes;
		this.checkoutDate = checkoutDate;
		this.returnDate = returnDate;
		this.requestDate = requestDate;
	}

	/**
	 * Constructs a new Request with a generated ID and default status set to
	 * PENDING The request date is set to the current date
	 *
	 * @param userId       the user ID of the requester
	 * @param equipmentId  the ID of the equipment being requested
	 * @param location     the location for equipment usage
	 * @param notes        additional notes
	 * @param checkoutDate the desired checkout date
	 * @param returnDate   the expected return date
	 */
	public Request(String userId, String equipmentId, String location, String notes, LocalDate checkoutDate,
			LocalDate returnDate) {
		this.id = UUID.randomUUID().toString();
		this.userId = userId;
		this.equipmentId = equipmentId;
		this.location = location;
		this.notes = notes;
		this.checkoutDate = checkoutDate;
		this.returnDate = returnDate;
		this.requestDate = LocalDate.now();
		this.status = Request_Status.PENDING.name();
	}

	/**
	 * Attempts to approve the request if there is no date conflict with existing
	 * approved requests
	 *
	 * @return true if the request was successfully approved; false if a conflict
	 *         exists
	 */
	public boolean approve() {
		RequestDao dao = new RequestDao();
		if (dao.hasDateConflict(this.equipmentId, this.checkoutDate, this.returnDate))
			return false;
		else {
			this.status = Request_Status.APPROVED.name();
			return true;
		}
	}

	/**
	 * Declines the request by setting its status to DECLINED
	 */
	public void decline() {
		this.status = Request_Status.DECLINED.name();
	}

	/** @return the unique ID of the request */
	public String getId() {
		return id;
	}

	/** @return the ID of the user who made the request */
	public String getUserId() {
		return userId;
	}

	/** @return the ID of the equipment being requested */
	public String getEquipmentId() {
		return equipmentId;
	}

	/** @return the location where the equipment is intended to be used */
	public String getLocation() {
		return location;
	}

	/** @return any additional notes about the request */
	public String getNotes() {
		return notes;
	}

	/** @return the current status of the request */
	public String getStatus() {
		return status;
	}

	/** @return the date the request was made */
	public LocalDate getRequestDate() {
		return requestDate;
	}

	/** @return the date the equipment is intended to be checked out */
	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	/** @return the date the equipment is intended to be returned */
	public LocalDate getReturnDate() {
		return returnDate;
	}

	/** @param id sets the unique ID of the request */
	public void setId(String id) {
		this.id = id;
	}

	/** @param userId sets the user ID of the requester */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** @param equipmentId sets the ID of the equipment being requested */
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	/** @param location sets the usage location of the equipment */
	public void setLocation(String location) {
		this.location = location;
	}

	/** @param notes sets any additional notes */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/** @param status sets the status of the request */
	public void setStatus(String status) {
		this.status = status;
	}

	/** @param requestDate sets the date the request was made */
	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}

	/** @param checkoutDate sets the desired checkout date */
	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	/** @param returnDate sets the expected return date */
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}
