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

/**
 * EquipmentDao provides data access operations for interacting with the
 * 'equipment' and 'checkoutLog' tables in the database. It includes CRUD
 * operations and a logging method. All methods are thread-safe using read and
 * write locks from MyLock.
 */

public class EquipmentDao {
	// Constants for equipment table column names
	private static final String equipmentColId = "id";
	private static final String equipmentColName = "itemName";
	private static final String equipmentColIsOperational = "isOperational";
	private static final String equipmentColLocation = "location";
	private static final String equipmentColImagePath = "imagePath";
	private static final String equipmentColNotes = "notes";
	private static final String equipmentColReturnDate = "returnDate";

	// Constants for checkout log table column names
	private static final String logColId = "id";
	private static final String logColItemId = "itemId";
	private static final String logColUserId = "userId";
	private static final String logColCheckoutDate = "checkoutDate";
	private static final String logColReturnDate = "returnDate";

	/**
	 * Retrieves all equipment records from the database
	 * 
	 * @return a map of equipment UUIDs to Equipment objects
	 */
	public Map<UUID, Equipment> getAllEquipment() {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM equipment";
			Map<UUID, Equipment> equipmentList = new HashMap<>();
			String id = null, name, location, imagePath, notes, isOperational;
			LocalDate returnDate;

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql);
					ResultSet results = statement.executeQuery()) {

				while (results.next()) {
					id = results.getString(equipmentColId);
					name = results.getString(equipmentColName);
					isOperational = results.getString(equipmentColIsOperational);
					location = results.getString(equipmentColLocation);
					imagePath = results.getString(equipmentColImagePath);
					notes = results.getString(equipmentColNotes);
					returnDate = results.getDate(equipmentColReturnDate) == null ? null
							: results.getDate(equipmentColReturnDate).toLocalDate();

					equipmentList.put(UUID.fromString(id),
							new Equipment(id, name, isOperational, location, imagePath, notes, returnDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return equipmentList;

		} finally {
			MyLock.readLock.unlock();
		}
	}

	/**
	 * Retrieves a single equipment item using its ID
	 * 
	 * @param id the ID of the equipment to retrieve
	 * @return the Equipment object if found, otherwise null
	 */
	public Equipment getEquipment(String id) {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM equipment WHERE " + equipmentColId + " = ?";

			Equipment equipment = null;

			String name, location, imagePath, notes, isOperational;
			LocalDate returnDate;

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, id);
				try (ResultSet results = statement.executeQuery()) {
					while (results.next()) {
						id = results.getString(equipmentColId);
						name = results.getString(equipmentColName);
						isOperational = results.getString(equipmentColIsOperational);
						location = results.getString(equipmentColLocation);
						imagePath = results.getString(equipmentColImagePath);
						notes = results.getString(equipmentColNotes);
						returnDate = results.getDate(equipmentColReturnDate) == null ? null
								: results.getDate(equipmentColReturnDate).toLocalDate();

						equipment = new Equipment(id, name, isOperational, location, imagePath, notes, returnDate);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return equipment;

		} finally {
			MyLock.readLock.unlock();
		}
	}

	/**
	 * Inserts a new equipment record to the database
	 * 
	 * @param equipment the Equipment object to insert
	 * @return true if insertion was successful, false otherwise
	 */
	public boolean createEquipment(Equipment equipment) {
		try {
			MyLock.writeLock.lock();

			String sql = String.format(
					"INSERT INTO equipment (%s, %s, %s, %s, %s, %s, %s) " + "VALUES (?, ?, ?, ?, ?, ?, ?)",
					equipmentColId, equipmentColName, equipmentColIsOperational, equipmentColLocation,
					equipmentColImagePath, equipmentColNotes, equipmentColReturnDate);

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, equipment.getId());
				statement.setString(2, equipment.getName());
				statement.setString(3, equipment.isOperationalString());
				statement.setString(4, equipment.getLocation());
				statement.setString(5, equipment.getImagePath());
				statement.setString(6, equipment.getNotes());
				statement.setDate(7,
						equipment.getReturnDate() == null ? null : Date.valueOf(equipment.getReturnDate()));

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

	/**
	 * Updates an existing equipment record
	 * 
	 * @param equipment the Equipment object to update
	 * @return true if update was successful, false otherwise
	 */
	public boolean updateEquipment(Equipment equipment) {
		try {
			MyLock.writeLock.lock();

			String sql = "UPDATE equipment SET " + equipmentColName + " = ?, " + equipmentColIsOperational + " = ?, "
					+ equipmentColLocation + " = ?, " + equipmentColImagePath + " = ?, " + equipmentColNotes + " = ?, "
					+ equipmentColReturnDate + " = ? " + "WHERE " + equipmentColId + " = ?";

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, equipment.getName());
				statement.setString(2, equipment.isOperationalString());
				statement.setString(3, equipment.getLocation());
				statement.setString(4, equipment.getImagePath());
				statement.setString(5, equipment.getNotes());
				statement.setDate(6,
						equipment.getReturnDate() == null ? null : Date.valueOf(equipment.getReturnDate()));
				statement.setString(7, equipment.getId());

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

	/**
	 * Deletes an equipment record using its ID
	 * 
	 * @param id the ID of the equipment to delete
	 * @return true if deletion was successful, false otherwise
	 */
	public boolean deleteEquipment(String id) {
		try {
			MyLock.writeLock.lock();

			String sql = "DELETE FROM equipment WHERE id = ?";

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

	/**
	 * Creates a new equipment record or updates it if it already exists
	 * 
	 * @param equipment the Equipment object to insert or update
	 * @return true if operation was successful, false otherwise
	 */
	public boolean createOrUpdateEquipment(Equipment equipment) {
		if (equipment != null && getAllEquipment().containsKey(equipment.getId())) {
			return updateEquipment(equipment);
		} else if (equipment != null) {
			return createEquipment(equipment);
		}

		return false;
	}

	/**
	 * Logs a checkout record into the checkoutLog table
	 * 
	 * @param itemId       the equipment item ID
	 * @param userId       the ID of the user checking out the equipment
	 * @param checkoutDate the date the equipment was checked out
	 * @param returnDate   the expected return date of the equipment
	 */
	public void logCheckout(String itemId, int userId, Date checkoutDate, Date returnDate) {
		try {
			MyLock.writeLock.lock();

			String sql = String.format("INSERT INTO checkoutLog (%s, %s, %s, %s)" + "VALUES (?, ?, ?, ?)", logColItemId,
					logColUserId, logColCheckoutDate, logColReturnDate);

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, itemId);
				statement.setInt(2, userId);
				statement.setDate(3, checkoutDate);
				statement.setDate(4, returnDate);

				statement.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			MyLock.writeLock.unlock();
		}
	}
}
