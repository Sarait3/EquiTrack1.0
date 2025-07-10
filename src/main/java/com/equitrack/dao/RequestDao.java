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

public class RequestDao {
	// Constants for equipment table column names
	private static final String requestColId = "id";
	private static final String requestColUserId = "userId";
	private static final String requestColEquipmentId = "equipmentId";
	private static final String requestColRequestDate = "requestDate";
	private static final String requestColCheckoutDate = "checkoutDate";
	private static final String requestColReturnDate = "returnDate";
	private static final String requestColLocation = "location";
	private static final String requestColNotes = "notes";
	private static final String requestColStatus = "status";

	public Map<UUID, Request> getAllRequests() {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM requests";
			Map<UUID, Request> requestList = new HashMap<>();
			String id, equipmentId, status, location, notes;
			int userId;
			LocalDate requestDate, checkoutDate, returnDate;

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql);
					ResultSet results = statement.executeQuery()) {

				while (results.next()) {
					id = results.getString(requestColId);
					userId = results.getInt(requestColUserId);
					equipmentId = results.getString(requestColEquipmentId);
					status = results.getString(requestColStatus);
					location = results.getString(requestColLocation);
					notes = results.getString(requestColNotes);
					requestDate = results.getDate(requestColRequestDate).toLocalDate();
					checkoutDate = results.getDate(requestColCheckoutDate).toLocalDate();
					returnDate = results.getDate(requestColReturnDate).toLocalDate();

					requestList.put(UUID.fromString(id), new Request(id, userId, equipmentId, status, location, notes,
							requestDate, checkoutDate, returnDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return requestList;

		} finally {
			MyLock.readLock.unlock();
		}
	}


	public Map<UUID, Request> getRequestsByUserId(int userId) {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM requests WHERE " + requestColUserId + " = ?";
			Map<UUID, Request> userRequests = new HashMap<>();

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setInt(1, userId);
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

		} finally {
			MyLock.readLock.unlock();
		}
	}

	public Request getRequest(String id) {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM requests WHERE " + requestColId + " = ?";

			Request request = null;
			int userId;
			String equipmentId, status, location, notes;
			LocalDate requestDate, checkoutDate, returnDate;

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, id);
				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						id = results.getString(requestColId);
						userId = results.getInt(requestColUserId);
						equipmentId = results.getString(requestColEquipmentId);
						status = results.getString(requestColStatus);
						location = results.getString(requestColLocation);
						notes = results.getString(requestColNotes);
						requestDate = results.getDate(requestColRequestDate).toLocalDate();
						checkoutDate = results.getDate(requestColCheckoutDate).toLocalDate();
						returnDate = results.getDate(requestColReturnDate).toLocalDate();

						request = new Request(id, userId, equipmentId, status, location, notes, requestDate,
								checkoutDate, returnDate);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return request;

		} finally {
			MyLock.readLock.unlock();
		}
	}

	public boolean createRequest(Request request) {
		try {
			MyLock.writeLock.lock();

			String sql = String.format(
					"INSERT INTO requests (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
					requestColId, requestColUserId, requestColEquipmentId, requestColStatus, requestColLocation,
					requestColNotes, requestColRequestDate, requestColCheckoutDate, requestColReturnDate);

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, request.getId());
				statement.setInt(2, request.getUserId());
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
		} finally {
			MyLock.writeLock.unlock();
		}
	}

	public User getUserForRequest(String requestId) {
		try {
			MyLock.readLock.lock();
			Request request = getRequest(requestId);
			if (request != null) {
				UserDao userDao = new UserDao();
				return userDao.getUserById(request.getUserId());
			}
		} finally {
			MyLock.readLock.unlock();
		}
		return null;
	}

	public Equipment getEquipmentForRequest(String requestId) {
		try {
			MyLock.readLock.lock();
			Request request = getRequest(requestId);
			if (request != null) {
				EquipmentDao equipmentDao = new EquipmentDao();
				return equipmentDao.getEquipment(request.getEquipmentId());
			}
		} finally {
			MyLock.readLock.unlock();
		}
		return null;
	}

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
				int userId = rs.getInt("userId");
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


	public boolean updateRequest(Request request) {
		try {
			MyLock.writeLock.lock();

			String sql = "UPDATE requests SET " + requestColStatus + " = ?, " + requestColLocation + " = ?, "
					+ requestColNotes + " = ?, " + requestColRequestDate + " = ?, " + requestColCheckoutDate + " = ?, "
					+ requestColReturnDate + " = ? " + "WHERE " + requestColId + " = ?";

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

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
		} finally {
			MyLock.writeLock.unlock();
		}
	}

	public boolean deleteRequest(String id) {
		try {
			MyLock.writeLock.lock();

			String sql = "DELETE FROM requests WHERE id = ?";

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, id);
				statement.execute();

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		} finally {
			MyLock.writeLock.unlock();
		}
	}
}
