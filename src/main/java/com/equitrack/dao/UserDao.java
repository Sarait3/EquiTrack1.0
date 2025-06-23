package com.equitrack.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.equitrack.model.User;

public class UserDao {
	private static final String userColId = "id";
	private static final String userColRole = "userRole";
	private static final String userColFName = "fName";
	private static final String userColLName = "lName";
	private static final String userColEmail = "email";
	private static final String userColPass = "password";
	
	public static User getUser(String columnName, String columnData) {
		try {
			MyLock.readLock.lock();

			String sql = String.format("SELECT * FROM users WHERE %s like ?;", columnName);
			int id;
			String fName, lName, email, password, role;

			try {
				Connection conn = DBConnection.getConnection();


				PreparedStatement statement = conn.prepareStatement(sql);
				statement.setString(1, columnData);
				ResultSet results = statement.executeQuery();


				if (results.next()) {
					id = results.getInt(userColId);
					role = results.getString(userColRole);
					fName = results.getString(userColFName);
					lName = results.getString(userColLName);
					email = results.getString(userColEmail);
					password = results.getString(userColPass);

					return new User(id, role, fName, lName, email, password);
				}

				results.close();
				statement.close();
				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

			return null;

		} finally {
			MyLock.readLock.unlock();
		}
	}
}
