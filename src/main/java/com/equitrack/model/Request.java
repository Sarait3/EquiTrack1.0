package com.equitrack.model;

import java.time.LocalDate;
import java.util.UUID;

import com.equitrack.dao.RequestDao;

public class Request {
	private String id, equipmentId, location, notes, status;
	int userId;
	private LocalDate requestDate, checkoutDate, returnDate;

	public Request(String id, int userId, String equipmentId, String status, String location, String notes,
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

	public Request(int userId, String equipmentId, String location, String notes, LocalDate checkoutDate,
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

	public boolean approve() {
		RequestDao dao = new RequestDao();
		if (dao.hasDateConflict(this.equipmentId, this.checkoutDate, this.returnDate))
			return false;
		else {
			this.status = Request_Status.APPROVED.name();
			return true;
		}

	}

	public void decline() {
		this.status = Request_Status.DECLINED.name();
	}

	public String getId() {
		return id;
	}

	public int getUserId() {
		return userId;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public String getLocation() {
		return location;
	}

	public String getNotes() {
		return notes;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setRequestDate(LocalDate requestDate) {
		this.requestDate = requestDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}
