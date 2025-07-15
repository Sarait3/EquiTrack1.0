package com.equitrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.equitrack.model.Equipment;
import com.equitrack.model.User;

/**
 * UserDao provides data access methods for retrieving user records from the
 * users table in the database
 */
public class UserDao {
	// Constants for column names in the 'users' table
	private static final String userColId = "id";
	private static final String userColRole = "userRole";
	private static final String userColFName = "fName";
	private static final String userColLName = "lName";
	private static final String userColEmail = "email";
	private static final String userColPass = "password";

	/**
	 * Retrieves a user from the database using their ID
	 * 
	 * @param userId the user ID
	 * @return a User object if found, null otherwise
	 */
	public User getUserById(String userId) {
		return getUser(userColId, userId);
	}

	/**
	 * Retrieves a user from the database using their email
	 * 
	 * @param userEmail the email address of the user
	 * @return a User object if found, null otherwise
	 */
	public User getUserByEmail(String userEmail) {
		return getUser(userColEmail, userEmail);
	}

	/**
	 * Internal helper method that retrieves a user using a given column name and
	 * value. Used by both getUserById and getUserByEmail
	 * 
	 * @param columnName the column name
	 * @param columnData the value to match in the specified column
	 * @return a User object if a match is found, null otherwise
	 */
	private User getUser(String columnName, String columnData) {
		try {
			MyLock.readLock.lock();

			String sql = String.format("SELECT * FROM users WHERE %s = ?;", columnName);

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {

				statement.setString(1, columnData);
				try (ResultSet results = statement.executeQuery()) {

					if (results.next()) {
						return new User(results.getString(userColId), results.getString(userColRole),
								results.getString(userColFName), results.getString(userColLName),
								results.getString(userColEmail), results.getString(userColPass));
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return null;

		} finally {
			MyLock.readLock.unlock();
		}
	}
	
	public Map<UUID, User> getAllUsers() {
		try {
			MyLock.readLock.lock();

			String sql = "SELECT * FROM users";
			Map<UUID, User> userList = new HashMap<>();
			String id = null, role, fName, lName, email, password;

			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql);
					ResultSet results = statement.executeQuery()) {

				while (results.next()) {
					id = results.getString(userColId);
					role = results.getString(userColRole);
					fName = results.getString(userColFName);
					lName = results.getString(userColLName);
					email = results.getString(userColEmail);
					password = results.getString(userColPass);

					userList.put(UUID.fromString(id),
							new User(id, role, fName, lName, email, password));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return userList;

		} finally {
			MyLock.readLock.unlock();
		}
		
	}
	
	public boolean createUser(User user) {
		try {
			MyLock.writeLock.lock();
			
			String sql = String.format(
					"INSERT INTO users (%s, %s, %s, %s, %s, %s)"
					+ "VALUES (?, ?, ?, ?, ?, ?)", 
					userColId, userColRole, userColFName, userColLName, userColEmail, userColPass);
			
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {
				
				statement.setString(1, user.getId());
				statement.setString(2, user.getRole());
				statement.setString(3, user.getFName());
				statement.setString(4, user.getLName());
				statement.setString(5, user.getEmail());
				statement.setString(6, user.getPassword());
				
				statement.execute();
				
				return true;
				
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			} 
		
		} finally {
			MyLock.writeLock.unlock();
		}
	}
	
	public boolean updateUser(User user) {
		try {
			MyLock.writeLock.lock();
			
			String sql = String.format("UPDATE users SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ?"
					+ "WHERE %s = ?", 
					userColRole, userColFName, userColLName, userColEmail, userColPass, userColId);
			
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {
				
				statement.setString(1, user.getRole());
				statement.setString(2, user.getFName());
				statement.setString(3, user.getLName());
				statement.setString(4, user.getEmail());
				statement.setString(5, user.getPassword());
				statement.setString(6, user.getId());
				
				statement.execute();
				
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} finally {
			MyLock.writeLock.unlock();
		}
	}
	
	public boolean deleteUser(String id) {
		try {
			MyLock.writeLock.lock();
			
			String sql = String.format("DELETE FROM users WHERE %s LIKE '%s'", userColId, id);
			
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement statement = conn.prepareStatement(sql)) {
				
				statement.execute();
				
				return true;
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		} finally {
			MyLock.writeLock.unlock();
		}
	}
}
