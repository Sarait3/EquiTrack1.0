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
	private static final String userColRole = "userRole";
	private static final String userColFName = "fName";
	private static final String userColLName = "lName";
	private static final String userColEmail = "email";
	private static final String userColPass = "password";

	public User getUserById(String userId) {
		return getUser(userColId, userId);
	}

	public User getUserByEmail(String userEmail) {
		return getUser(userColEmail, userEmail);
	}

	private User getUser(String columnName, String columnData) {
		UserBuilder user = new UserBuilder();

		String sql = String.format("SELECT * FROM users WHERE %s = ?;", columnName);

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.setString(1, columnData);
			try (ResultSet results = statement.executeQuery()) {
				if (results.next()) {
					return user.setId(results.getString(userColId))
						.setRole(results.getString(userColRole))
						.setFName(results.getString(userColFName))
						.setLName(results.getString(userColLName))
						.setEmail(results.getString(userColEmail))
						.setPassword(results.getString(userColPass))
						.createUser();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Map<UUID, User> getAllUsers() {
		String sql = "SELECT * FROM users";
		Map<UUID, User> userMap = new HashMap<>();

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql);
			 ResultSet results = statement.executeQuery()) {

			while (results.next()) {
				UserBuilder user = new UserBuilder();

				user.setId(results.getString(userColId))
					.setRole(results.getString(userColRole))
					.setFName(results.getString(userColFName))
					.setLName(results.getString(userColLName))
					.setEmail(results.getString(userColEmail))
					.setPassword(results.getString(userColPass));

				userMap.put(UUID.fromString(results.getString(userColId)), user.createUser());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userMap;
	}

	public boolean createUser(User user) {
		String sql = String.format(
				"INSERT INTO users (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
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
	}

	public boolean updateUser(User user) {
		String sql = String.format(
				"UPDATE users SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
				userColRole, userColFName, userColLName, userColEmail, userColPass, userColId, userColId);

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql)) {

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

	public boolean deleteUser(String id) {
		String sql = String.format("DELETE FROM users WHERE %s LIKE '%s'", userColId, id);

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement statement = conn.prepareStatement(sql)) {

			statement.execute();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
