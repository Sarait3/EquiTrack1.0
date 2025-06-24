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
	private static final String equipmentColId = "id";
	private static final String equipmentColName = "itemName";
	private static final String equipmentColIsAvailable = "isAvailable";
	private static final String equipmentColLocation = "location";
	private static final String equipmentColImagePath = "imagePath";
	private static final String equipmentColNotes = "notes";
	private static final String equipmentColReturnDate = "returnDate";

	public Map<UUID, Equipment> getAllEquipment() {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM equipment";
			Map<UUID, Equipment> equipmentList = new HashMap<>();
			String id = null, name, location, imagePath, notes, isAvailable;
			LocalDate returnDate;

			try {
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet results = statement.executeQuery();

				while (results.next()) {
					id = results.getString("id");
					name = results.getString("name");
					isAvailable = results.getString("isAvailable");
					location = results.getString("location");
					imagePath = results.getString("imagePath");
					notes = results.getString("notes");
					returnDate = results.getDate(equipmentColReturnDate).toLocalDate();

					equipmentList.put(UUID.fromString(id), new Equipment(id, name, isAvailable, location, imagePath, notes, returnDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return equipmentList;

		} finally {
			MyLock.readLock.unlock();
		}
	}

	public Equipment getEquipment(String id) {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM equipment WHERE " + equipmentColId + " = ?";

			Equipment equipment = null;

			String name, location, imagePath, notes, isAvailable;
			LocalDate returnDate;

			try {
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, id);
				ResultSet results = statement.executeQuery();

				while (results.next()) {
					id = results.getString(equipmentColId);
					name = results.getString(equipmentColName);
					isAvailable = results.getString(equipmentColIsAvailable);
					location = results.getString(equipmentColLocation);
					imagePath = results.getString(equipmentColImagePath);
					notes = results.getString(equipmentColNotes);
					returnDate = results.getDate(equipmentColReturnDate).toLocalDate();

					equipment = new Equipment(id, name, isAvailable, location, imagePath, notes, returnDate);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return equipment;

		} finally {
			MyLock.readLock.unlock();
		}
	}

	public boolean createEquipment(Equipment equipment) {
		try {
			MyLock.writeLock.lock();

			String sql = String.format("INSERT INTO equipment (%s, %s, %s, %s, %s, %s, %s) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)", equipmentColId, equipmentColName, equipmentColIsAvailable, 
					equipmentColLocation, equipmentColImagePath, equipmentColNotes, equipmentColReturnDate);

			try {
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);

				statement.setString(1, equipment.getId());
				statement.setString(2, equipment.getName());
				statement.setString(3, equipment.isAvailableString());
				statement.setString(4, equipment.getLocation());
				statement.setString(5, equipment.getImagePath());
				statement.setString(6, equipment.getNotes());
				statement.setDate(7, Date.valueOf(equipment.getReturnDate()));

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

	public boolean updateEquipment(Equipment equipment) {
		try {
			MyLock.writeLock.lock();

			String sql = "UPDATE equipment SET "
					+ equipmentColName + " = ?, "
					+ equipmentColIsAvailable + " = ?, "
					+ equipmentColLocation + " = ?, "
					+ equipmentColImagePath + " = ?, "
					+ equipmentColNotes + " = ?, "
					+ equipmentColReturnDate + " = ?"
					+ "WHERE " + equipmentColId + " = ?";

			try {
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);

				statement.setString(1, equipment.getName());
				statement.setString(2, equipment.isAvailableString());
				statement.setString(3, equipment.getLocation());
				statement.setString(4, equipment.getImagePath());
				statement.setString(5, equipment.getNotes());
				statement.setDate(6, Date.valueOf(equipment.getReturnDate()));
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

	public boolean deleteEquipment(String id) {
		try {
			MyLock.writeLock.lock();

			String sql = "DELETE FROM equipment WHERE id = ?";

			try {
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);

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

	public boolean createOrUpdateteEquipment(Equipment equipment) {
		if (equipment != null && getAllEquipment().containsKey(equipment.getId())) {
			return updateEquipment(equipment);
		} else if (equipment != null){
			return createEquipment(equipment);
		}

		return false;
	}

	public void logCheckout(String itemId, int userId, Date checkoutDate, Date returnDate) {
		try {
			MyLock.writeLock.lock();

			String sql = "INSERT INTO checkoutLog (itemId, userId, checkoutDate)"
					+ "VALUES (?, ?, ?, ?)";

			try {
				Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);

				statement.setInt(1, userId);
				statement.setString(2, itemId);
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

