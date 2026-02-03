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
 * Data Access Object (DAO) for performing CRUD operations and logging on the
 * Equipment database table. Handles interaction between the application and the
 * equipment-related database tables
 */
public class EquipmentDao {
	// Constants for equipment table column names
	private static final String equipmentColId = "id";
	private static final String equipmentColName = "itemname";
	private static final String equipmentColIsOperational = "isoperational";
	private static final String equipmentColLocation = "location";
	private static final String equipmentColImagePath = "imagePath";
	private static final String equipmentColNotes = "notes";
	private static final String equipmentColReturnDate = "returndate";

	/**
	 * Retrieves all equipment records from the database
	 *
	 * @return a map of UUID to Equipment objects
	 */
	public Map<UUID, Equipment> getAllEquipment() {
		String sql = "SELECT * FROM equipment";
		Map<UUID, Equipment> equipmentList = new HashMap<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet results = statement.executeQuery()) {

			while (results.next()) {
				String id = results.getString(equipmentColId);
				String name = results.getString(equipmentColName);
				String isOperational = results.getString(equipmentColIsOperational);
				String location = results.getString(equipmentColLocation);
				String imagePath = results.getString(equipmentColImagePath);
				String notes = results.getString(equipmentColNotes);
				LocalDate returnDate = results.getDate(equipmentColReturnDate) == null ? null
						: results.getDate(equipmentColReturnDate).toLocalDate();

				Equipment equipment = new Equipment.Builder().setId(id).setName(name).setOperational(isOperational)
						.setLocation(location).setImagePath(imagePath).setNotes(notes).setReturnDate(returnDate)
						.build();

				equipmentList.put(UUID.fromString(id), equipment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipmentList;
	}

	/**
	 * Retrieves a single Equipment record by its ID
	 *
	 * @param id the ID of the equipment
	 * @return the Equipment object or null if not found
	 */
	public Equipment getEquipment(String id) {
		String sql = "SELECT * FROM equipment WHERE " + equipmentColId + " = ?";
		Equipment equipment = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, id);
			try (ResultSet results = statement.executeQuery()) {
				while (results.next()) {
					String name = results.getString(equipmentColName);
					String isOperational = results.getString(equipmentColIsOperational);
					String location = results.getString(equipmentColLocation);
					String imagePath = results.getString(equipmentColImagePath);
					String notes = results.getString(equipmentColNotes);
					LocalDate returnDate = results.getDate(equipmentColReturnDate) == null ? null
							: results.getDate(equipmentColReturnDate).toLocalDate();

					equipment = new Equipment.Builder().setId(id).setName(name).setOperational(isOperational)
							.setLocation(location).setImagePath(imagePath).setNotes(notes).setReturnDate(returnDate)
							.build();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipment;
	}

	/**
	 * Inserts a new equipment record into the database
	 *
	 * @param equipment the Equipment object to create
	 * @return true if the insertion was successful; false otherwise
	 */
	public boolean createEquipment(Equipment equipment) {
		String sql = String.format("INSERT INTO equipment (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
				equipmentColId, equipmentColName, equipmentColIsOperational, equipmentColLocation,
				equipmentColImagePath, equipmentColNotes, equipmentColReturnDate);

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, equipment.getId());
			statement.setString(2, equipment.getName());
			statement.setString(3, equipment.isOperationalString());
			statement.setString(4, equipment.getLocation());
			statement.setString(5, equipment.getImagePath());
			statement.setString(6, equipment.getNotes());
			statement.setDate(7, equipment.getReturnDate() == null ? null : Date.valueOf(equipment.getReturnDate()));

			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Updates an existing equipment record in the database
	 *
	 * @param equipment the Equipment object with updated values
	 * @return true if the update was successful; false otherwise
	 */
	public boolean updateEquipment(Equipment equipment) {
		String sql = "UPDATE equipment SET " + equipmentColName + " = ?, " + equipmentColIsOperational + " = ?, "
				+ equipmentColLocation + " = ?, " + equipmentColImagePath + " = ?, " + equipmentColNotes + " = ?, "
				+ equipmentColReturnDate + " = ? WHERE " + equipmentColId + " = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, equipment.getName());
			statement.setString(2, equipment.isOperationalString());
			statement.setString(3, equipment.getLocation());
			statement.setString(4, equipment.getImagePath());
			statement.setString(5, equipment.getNotes());
			statement.setDate(6, equipment.getReturnDate() == null ? null : Date.valueOf(equipment.getReturnDate()));
			statement.setString(7, equipment.getId());

			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Deletes an equipment record by its ID
	 *
	 * @param id the ID of the equipment to delete
	 * @return true if deletion was successful; false otherwise
	 */
	public boolean deleteEquipment(String id) {
		String sql = "DELETE FROM equipment WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, id);
			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Creates a new equipment record or updates the existing one if it already
	 * exists
	 *
	 * @param equipment the Equipment object to create or update
	 * @return true if the operation was successful; false otherwise
	 */
	public boolean createOrUpdateEquipment(Equipment equipment) {
		if (equipment != null && getAllEquipment().containsKey(equipment.getId())) {
			return updateEquipment(equipment);
		} else if (equipment != null) {
			return createEquipment(equipment);
		}
		return false;
	}

}
