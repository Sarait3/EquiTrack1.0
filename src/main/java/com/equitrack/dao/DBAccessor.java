package com.equitrack.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.equitrack.model.*;

public class DBAccessor {
	private static final String connString = "jdbc:mysql://localhost/EquiTrack";
	private static final String dbUsername = "dbUser";
	private static final String dbPassword = "dbPass";

	static private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	static private ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
	static private ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();


	static Connection conn = null;

	public static User getUser(String columnName, String columnData) {
		try {
			readLock.lock();
			
			String sql = String.format("SELECT * FROM users WHERE %s like ?;", columnName);
			int id = 0;
			String fName = null, 
					lName = null, 
					email = null, 
					password = null,
					role = null;

			try {
				conn = DriverManager.getConnection(connString, dbUsername, dbPassword);


				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, columnData);
				ResultSet results = statement.executeQuery();


				while (results.next()) {
					id = results.getInt("id");
					role = results.getString("role");
					fName = results.getString("fName");
					lName = results.getString("lname");
					email = results.getString("email");
					password = results.getString("password");
				}

				results.close();
				statement.close();
				conn.close();

				if (id != 0) {
					readLock.unlock();
					return new User(id, role, fName, lName, email, password);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return null;

		} finally {
			readLock.unlock();
		}
	}

	public static Map<UUID, Equipment> getAllEquipment() {
		try {
			readLock.lock();

			String sql = "SELECT * FROM equipment";
			Map<UUID, Equipment> equipmentList = new HashMap<>();
			String id = null;
			String name;
			boolean isAvailable;
			String location;
			String imagePath;
			String notes;

			try {
				conn = DriverManager.getConnection(connString, dbUsername, dbPassword);

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet results = statement.executeQuery();

				while (results.next()) {
					if (id != null) {
						id = results.getString("id");
						name = results.getString("name");
						isAvailable = results.getBoolean("isAvailable");
						location = results.getString("location");
						imagePath = results.getString("imagePath");
						notes = results.getString("notes");

						equipmentList.put(UUID.fromString(id), new Equipment(id, name, isAvailable, location, imagePath, notes));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return equipmentList;

		} finally {
			readLock.unlock();
		}
	}

	public static Equipment getEquipment(String id) {
		try {
			readLock.lock();

			String sql = "SELECT * FROM equipment WHERE id = ?";
			Equipment equipment = null;
			String name;
			boolean isAvailable;
			String location;
			String imagePath;
			String notes;

			try {
				conn = DriverManager.getConnection(connString, dbUsername, dbPassword);

				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet results = statement.executeQuery();

				while (results.next()) {
					if (id != null) {
						id = results.getString("id");
						name = results.getString("name");
						isAvailable = results.getBoolean("isAvailable");
						location = results.getString("location");
						imagePath = results.getString("imagePath");
						notes = results.getString("notes");

						equipment = new Equipment(id, name, isAvailable, location, imagePath, notes);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return equipment;

		} finally {
			readLock.unlock();
		}
	}
	
	public static boolean createEquipment(Equipment equipment) {
		try {
			writeLock.lock();
			
			String sql = "INSERT INTO equipment (id, name, isAvailable, location, imagePath, notes"
					+ "VALUES (?, ?, ?, ?, ?, ?";
			
			try {
				Connection conn = DriverManager.getConnection(connString, dbUsername, dbPassword);
				
				PreparedStatement statement = conn.prepareStatement(sql);
				
				statement.setString(1, equipment.getId());
				statement.setString(2, equipment.getName());
				statement.setBoolean(3, equipment.isAvailable());
				statement.setString(4, equipment.getLocation());
				statement.setString(5, equipment.getImagePath());
				statement.setString(6, equipment.getNotes());
				
				statement.executeQuery();
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		} finally {
			writeLock.unlock();
		}
	}
	
	public static boolean updateEquipment(Equipment equipment) {
		try {
			writeLock.lock();
			
			String sql = "UPDATE equipment SET name = ?, "
					+ "isAvailable = ?, "
					+ "location =?, "
					+ "imagePath = ?, "
					+ "notes = ?"
					+ "WHERE id = ?";
			
			try {
				Connection conn = DriverManager.getConnection(sql, dbUsername, dbPassword);
				
				PreparedStatement statement = conn.prepareStatement(sql);
				
				statement.setString(1, equipment.getName());
				statement.setBoolean(2, equipment.isAvailable());
				statement.setString(3, equipment.getLocation());
				statement.setString(4, equipment.getImagePath());
				statement.setString(5, equipment.getNotes());
				statement.setString(6, equipment.getId());
				
				statement.executeQuery();
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		} finally {
			writeLock.unlock();
		}
	}
	
	public static boolean deleteEquipment(String id) {
		try {
			writeLock.lock();
			
			String sql = "DELETE FROM equipment WHERE id = ?";
			
			try {
				Connection conn = DriverManager.getConnection(connString, dbUsername, dbPassword);
				
				PreparedStatement statement = conn.prepareStatement(sql);
				
				statement.setString(1, id);
				statement.executeQuery();
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		} finally {
			writeLock.unlock();
		}
	}
	
	public static boolean createOrUpdateteEquipment(Equipment equipment) {
		if (equipment != null && getAllEquipment().containsKey(equipment.getId())) {
			return updateEquipment(equipment);
		} else if (equipment != null){
			return createEquipment(equipment);
		}
		
		return false;
	}
	
	public static void logCheckout(String itemId, int userId, Date checkoutDate) {
		try {
			writeLock.lock();
			
			String sql = "INSERT INTO checkoutLog (itemId, userId, checkoutDate)"
					+ "VALUES (?, ?, ?)";
			
			try {
				Connection conn = DriverManager.getConnection(connString, dbUsername, dbPassword);
				
				PreparedStatement statement = conn.prepareStatement(sql);
				
				statement.setInt(1, userId);
				statement.setString(2, itemId);
				statement.setDate(3, checkoutDate);
				
				statement.executeQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			writeLock.unlock();
		}
	}
}
