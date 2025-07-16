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
	private static final String logColItemId = "itemId";
	private static final String logColUserId = "userId";
	private static final String logColCheckoutDate = "checkoutDate";
	private static final String logColReturnDate = "returnDate";

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

				Equipment equipment = new Equipment.Builder()
						.setId(id)
						.setName(name)
						.setOperational(isOperational)
						.setLocation(location)
						.setImagePath(imagePath)
						.setNotes(notes)
						.setReturnDate(returnDate)
						.build();

				equipmentList.put(UUID.fromString(id), equipment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipmentList;
	}

	public Equipment getEquipment(String id) {
		String sql = "SELECT * FROM equipment WHERE " + equipmentColId + " = ?";
		Equipment equipment = null;

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql)) {

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

					equipment = new Equipment.Builder()
							.setId(id)
							.setName(name)
							.setOperational(isOperational)
							.setLocation(location)
							.setImagePath(imagePath)
							.setNotes(notes)
							.setReturnDate(returnDate)
							.build();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return equipment;
	}

	public boolean createEquipment(Equipment equipment) {
		String sql = String.format(
				"INSERT INTO equipment (%s, %s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
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
			statement.setDate(7, equipment.getReturnDate() == null ? null : Date.valueOf(equipment.getReturnDate()));

			statement.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean updateEquipment(Equipment equipment) {
		String sql = "UPDATE equipment SET " + equipmentColName + " = ?, " + equipmentColIsOperational + " = ?, "
				+ equipmentColLocation + " = ?, " + equipmentColImagePath + " = ?, " + equipmentColNotes + " = ?, "
				+ equipmentColReturnDate + " = ? WHERE " + equipmentColId + " = ?";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql)) {

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

	public boolean deleteEquipment(String id) {
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
	}

	public boolean createOrUpdateEquipment(Equipment equipment) {
		if (equipment != null && getAllEquipment().containsKey(equipment.getId())) {
			return updateEquipment(equipment);
		} else if (equipment != null) {
			return createEquipment(equipment);
		}
		return false;
	}

	public void logCheckout(String itemId, int userId, Date checkoutDate, Date returnDate) {
		String sql = String.format("INSERT INTO checkoutLog (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
				logColItemId, logColUserId, logColCheckoutDate, logColReturnDate);

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
	}
}
