package com.equitrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	public User getUserById(int userId) {
		return getUser(userColId, Integer.toString(userId));
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
						return new User(results.getInt(userColId), results.getString(userColRole),
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
}
