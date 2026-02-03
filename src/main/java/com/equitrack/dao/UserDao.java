package com.equitrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.equitrack.model.User;
import com.equitrack.service.UserBuilder;

public class UserDao {
	// Constants for column names in the 'users' table
	private static final String userColId = "id";
	private static final String userColRole = "userrole";
	private static final String userColFName = "fname";
	private static final String userColLName = "lname";
	private static final String userColEmail = "email";
	private static final String userColPass = "password";

	/**
	 * Returns a User object from the database by calling the private getUser()
	 * method and passing the userColId constant and id of the desired user.
	 * 
	 * @param userId The id of the desired user
	 * @return Returns a User object or null
	 */
	public User getUserById(String userId) {
		return getUser(userColId, userId);
	}

	/**
	 * Returns a User object from the database by calling the private getUser()
	 * method and passing the userColEmail constant and the email of the desired
	 * user.
	 * 
	 * @param userEmail The email of the desired user
	 * @return Returns a User object or null
	 */
	public User getUserByEmail(String userEmail) {
		return getUser(userColEmail, userEmail);
	}

	/**
	 * Instantiates and returns the first User object found in the database using
	 * the desired column of the users table and the content of that column for the
	 * desired user.
	 * 
	 * @param columnName The name of the column containing the desired user's data
	 * @param columnData The identifying data of the desired user
	 * @return Returns a User object or null
	 */
	private User getUser(String columnName, String columnData) {
		UserBuilder user = new UserBuilder();

		String sql = String.format("SELECT * FROM users WHERE %s = ?;", columnName);

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, columnData);
			try (ResultSet results = statement.executeQuery()) {
				if (results.next()) {
					return user.setId(results.getString(userColId)).setRole(results.getString(userColRole))
							.setFName(results.getString(userColFName)).setLName(results.getString(userColLName))
							.setEmail(results.getString(userColEmail)).setPassword(results.getString(userColPass))
							.createUser();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Returns a Map containing all users in the 'users' table with their UUID as
	 * the key and a User object generated using that user's data as the value.
	 * 
	 * @return returns a User object or null
	 */
	public Map<UUID, User> getAllUsers() {
		String sql = "SELECT * FROM users";
		Map<UUID, User> userMap = new HashMap<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement statement = conn.prepareStatement(sql);
				ResultSet results = statement.executeQuery()) {

			while (results.next()) {
				UserBuilder user = new UserBuilder();

				user.setId(results.getString(userColId)).setRole(results.getString(userColRole))
						.setFName(results.getString(userColFName)).setLName(results.getString(userColLName))
						.setEmail(results.getString(userColEmail)).setPassword(results.getString(userColPass));

				userMap.put(UUID.fromString(results.getString(userColId)), user.createUser());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userMap;
	}

	/**
	 * Adds a new user to the 'users' table using the fields of a User object.
	 * 
	 * @param user A User object representing the user to be added to the database
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean createUser(User user) {
		String sql = String.format("INSERT INTO users (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)", userColId,
				userColRole, userColFName, userColLName, userColEmail, userColPass);

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

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
	}

	/**
	 * Overwrites the data of a user in the 'users' table of the database using the
	 * fields of the provided User object.
	 * 
	 * @param user The User object to overwrite the database entry with
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean updateUser(User user) {
		String sql = String.format("UPDATE users SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
				userColRole, userColFName, userColLName, userColEmail, userColPass, userColId, userColId);

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, user.getRole());
			statement.setString(2, user.getFName());
			statement.setString(3, user.getLName());
			statement.setString(4, user.getEmail());
			statement.setString(5, user.getPassword());
			statement.setString(6, user.getId());
			statement.setString(7, user.getId());

			statement.execute();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Deletes the user with the provided id from the 'users' table of the database.
	 * 
	 * @param id The id of the user to delete
	 * @return Returns true if the operation was successful, false otherwise
	 */
	public boolean deleteUser(String id) {
		String sql = String.format("DELETE FROM users WHERE %s = ?", userColId);

		try (Connection conn = DBConnection.getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
			
			statement.setString(1, id);
			statement.execute();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
