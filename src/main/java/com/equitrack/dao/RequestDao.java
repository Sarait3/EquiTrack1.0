package com.equitrack.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.equitrack.model.Equipment;
import com.equitrack.model.Request;
import com.equitrack.model.User;

/**
 * Data Access Object (DAO) for interacting with the requests table in the
 * database. Provides methods for creating, retrieving, updating, and deleting
 * checkout requests, as well as utility methods for retrieving associated users
 * and equipment.
 */
public class RequestDao {
	// Constants for request table column names
	private static final String requestColId = "id";
	private static final String requestColUserId = "userId";
	private static final String requestColEquipmentId = "equipmentId";
	private static final String requestColRequestDate = "requestDate";
	private static final String requestColCheckoutDate = "checkoutDate";
	private static final String requestColReturnDate = "returnDate";
	private static final String requestColLocation = "location";
	private static final String requestColNotes = "notes";
	private static final String requestColStatus = "status";

	/**
	 * Retrieves all requests from the database
	 *
	 * @return a map of request IDs to Request objects
	 */
	public Map<UUID, Request> getAllRequests() {
		String sql = "SELECT * FROM requests";
		Map<UUID, Request> requestList = new HashMap<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet results = statement.executeQuery()) {

			while (results.next()) {
				String id = results.getString(requestColId);
				String userId = results.getString(requestColUserId);
				String equipmentId = results.getString(requestColEquipmentId);
				String status = results.getString(requestColStatus);
				String location = results.getString(requestColLocation);
				String notes = results.getString(requestColNotes);
				LocalDate requestDate = results.getDate(requestColRequestDate).toLocalDate();
				LocalDate checkoutDate = results.getDate(requestColCheckoutDate).toLocalDate();
				LocalDate returnDate = results.getDate(requestColReturnDate).toLocalDate();

				requestList.put(UUID.fromString(id), new Request(id, userId, equipmentId, status, location, notes,
						requestDate, checkoutDate, returnDate));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestList;
	}

	/**
	 * Retrieves all requests made by a specific user
	 *
	 * @param userId the ID of the user
	 * @return a map of request IDs to Request objects for the given user
	 */
	public Map<UUID, Request> getRequestsByUserId(String userId) {
		String sql = "SELECT * FROM requests WHERE " + requestColUserId + " = ?";
		Map<UUID, Request> userRequests = new HashMap<>();

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, userId);
			try (ResultSet results = statement.executeQuery()) {
				while (results.next()) {
					String id = results.getString(requestColId);
					String equipmentId = results.getString(requestColEquipmentId);
					String status = results.getString(requestColStatus);
					String location = results.getString(requestColLocation);
					String notes = results.getString(requestColNotes);
					LocalDate requestDate = results.getDate(requestColRequestDate).toLocalDate();
					LocalDate checkoutDate = results.getDate(requestColCheckoutDate).toLocalDate();
					LocalDate returnDate = results.getDate(requestColReturnDate).toLocalDate();

					Request request = new Request(id, userId, equipmentId, status, location, notes, requestDate,
							checkoutDate, returnDate);

					userRequests.put(UUID.fromString(id), request);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userRequests;
	}

	/**
	 * Retrieves a specific request by its ID
	 *
	 * @param id the ID of the request
	 * @return the matching Request object, or null if not found
	 */
	public Request getRequest(String id) {
		String sql = "SELECT * FROM requests WHERE " + requestColId + " = ?";
		Request request = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, id);
			try (ResultSet results = statement.executeQuery()) {
				while (results.next()) {
					String userId = results.getString(requestColUserId);
					String equipmentId = results.getString(requestColEquipmentId);
					String status = results.getString(requestColStatus);
					String location = results.getString(requestColLocation);
					String notes = results.getString(requestColNotes);
					LocalDate requestDate = results.getDate(requestColRequestDate).toLocalDate();
					LocalDate checkoutDate = results.getDate(requestColCheckoutDate).toLocalDate();
					LocalDate returnDate = results.getDate(requestColReturnDate).toLocalDate();

					request = new Request(id, userId, equipmentId, status, location, notes, requestDate, checkoutDate,
							returnDate);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return request;
	}

	/**
	 * Inserts a new request into the database
	 *
	 * @param request the Request object to insert
	 * @return true if the operation succeeded, false otherwise
	 */
	public boolean createRequest(Request request) {
		String sql = String.format(
				"INSERT INTO requests (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				requestColId, requestColUserId, requestColEquipmentId, requestColStatus, requestColLocation,
				requestColNotes, requestColRequestDate, requestColCheckoutDate, requestColReturnDate);

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, request.getId());
			statement.setString(2, request.getUserId());
			statement.setString(3, request.getEquipmentId());
			statement.setString(4, request.getStatus());
			statement.setString(5, request.getLocation());
			statement.setString(6, request.getNotes());
			statement.setDate(7, Date.valueOf(request.getRequestDate()));
			statement.setDate(8, Date.valueOf(request.getCheckoutDate()));
			statement.setDate(9, Date.valueOf(request.getReturnDate()));

			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Retrieves the User associated with a given request
	 *
	 * @param requestId the ID of the request
	 * @return the User who made the request, or null if not found
	 */
	public User getUserForRequest(String requestId) {
		Request request = getRequest(requestId);
		if (request != null) {
			UserDao userDao = new UserDao();
			return userDao.getUserById(request.getUserId());
		}
		return null;
	}

	/**
	 * Retrieves the Equipment associated with a given request
	 *
	 * @param requestId the ID of the request
	 * @return the Equipment item associated with the request, or null if not found
	 */
	public Equipment getEquipmentForRequest(String requestId) {
		Request request = getRequest(requestId);
		if (request != null) {
			EquipmentDao equipmentDao = new EquipmentDao();
			return equipmentDao.getEquipment(request.getEquipmentId());
		}
		return null;
	}

	/**
	 * Checks if a date range for a given equipment item conflicts with existing
	 * approved requests
	 *
	 * @param equipmentId the ID of the equipment item
	 * @param start       the proposed checkout date
	 * @param end         the proposed return date
	 * @return true if a conflict exists, false otherwise
	 */
	public boolean hasDateConflict(String equipmentId, LocalDate start, LocalDate end) {
		String sql = "SELECT COUNT(*) FROM requests WHERE equipmentId = ? AND status = 'approved' AND NOT (returnDate < ? OR checkoutDate > ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, equipmentId);
			stmt.setDate(2, Date.valueOf(start));
			stmt.setDate(3, Date.valueOf(end));
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getInt(1) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Retrieves all upcoming approved requests for a given equipment item
	 *
	 * @param equipmentId the ID of the equipment
	 * @return a map of request IDs to Request objects that are approved and have a
	 *         future checkout date
	 */
	public Map<UUID, Request> getUpcomingApprovedRequests(String equipmentId) {
		Map<UUID, Request> upcoming = new HashMap<>();
		String sql = "SELECT * FROM requests WHERE equipmentId = ? AND status = 'approved' AND checkoutDate >= ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			LocalDate today = LocalDate.now();
			stmt.setString(1, equipmentId);
			stmt.setDate(2, Date.valueOf(today));
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String userId = rs.getString("userId");
				String equipId = rs.getString("equipmentId");
				String status = rs.getString("status");
				String location = rs.getString("location");
				String notes = rs.getString("notes");
				LocalDate requestDate = rs.getDate("requestDate").toLocalDate();
				LocalDate checkoutDate = rs.getDate("checkoutDate").toLocalDate();
				LocalDate returnDate = rs.getDate("returnDate").toLocalDate();

				Request request = new Request(id, userId, equipId, status, location, notes, requestDate, checkoutDate,
						returnDate);
				upcoming.put(UUID.fromString(id), request);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return upcoming;
	}

	/**
	 * Updates an existing request's details in the database
	 *
	 * @param request the Request object with updated data
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateRequest(Request request) {
		String sql = "UPDATE requests SET " + requestColStatus + " = ?, " + requestColLocation + " = ?, "
				+ requestColNotes + " = ?, " + requestColRequestDate + " = ?, " + requestColCheckoutDate + " = ?, "
				+ requestColReturnDate + " = ? WHERE " + requestColId + " = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, request.getStatus());
			statement.setString(2, request.getLocation());
			statement.setString(3, request.getNotes());
			statement.setDate(4, Date.valueOf(request.getRequestDate()));
			statement.setDate(5, Date.valueOf(request.getCheckoutDate()));
			statement.setDate(6, Date.valueOf(request.getReturnDate()));
			statement.setString(7, request.getId());

			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Deletes a request by its ID from the database
	 *
	 * @param id the ID of the request to delete
	 * @return true if the deletion was successful, false otherwise
	 */
	public boolean deleteRequest(String id) {
		String sql = "DELETE FROM requests WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, id);
			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
