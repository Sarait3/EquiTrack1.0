package com.equitrack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection is a utility class that provides a method to establish a
 * connection to the EquiTrack MySQL database.
 */
public class DBConnection {

	// Database connection string and credentials (for local development)
	private static final String connString = "jdbc:mysql://localhost/equitrack";
	private static final String dbUsername = "dbUser";
	private static final String dbPassword = "dbPassword";

	/**
	 * Establishes and returns a new connection to the EquiTrack database.
	 *
	 * @return a new Connection object to the database
	 * @throws SQLException if a database access error occurs
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connString, dbUsername, dbPassword);
	}
}
