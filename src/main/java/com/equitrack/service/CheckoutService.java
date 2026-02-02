package com.equitrack.service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import com.equitrack.dao.EquipmentDao;
import com.equitrack.dao.RequestDao;
import com.equitrack.model.*;

/**
 * Service class responsible for managing checkout-related business logic
 */
public class CheckoutService {
	private EquipmentDao equipmentDao;
	private RequestDao requestDao;

	public CheckoutService() {
		equipmentDao = new EquipmentDao();
		requestDao = new RequestDao();
	}

	/**
	 * Returns equipment details by ID
	 */
	public Equipment getEquipment(String itemId) {
		return equipmentDao.getEquipment(itemId);
	}

	/**
	 * Returns approved upcoming requests for given equipment
	 */
	public Map<UUID, Request> getUpcomingApprovedRequests(String equipmentId) {
		return requestDao.getUpcomingApprovedRequests(equipmentId);
	}

	/**
	 * Creates a checkout request with possible auto-approval for manager/admin
	 */
	public boolean requestCheckout(User user, String itemId, String location, String notes,
			LocalDate checkoutDate, LocalDate returnDate) {
		if (requestDao.hasDateConflict(itemId, checkoutDate, returnDate)) {
			return false;
		}

		Request request = new Request(user.getId(), itemId, location, notes, checkoutDate, returnDate);
		if (user.getRole().equalsIgnoreCase("Admin") || user.getRole().equalsIgnoreCase("Manager")) {
			request.approve();
		}
		requestDao.createRequest(request);
		return true;
	}
}
