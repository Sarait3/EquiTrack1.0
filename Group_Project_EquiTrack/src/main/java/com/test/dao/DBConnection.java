package com.test.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for establishing a connection to the MySQL database.
 * Encapsulates the JDBC driver loading and connection string details.
 */
public class DBConnection {
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";
	private static final String CONN_STRING = "jdbc:mysql://localhost:3306/EquiTrack";

	/**
	 * Loads the JDBC driver and returns a new connection instance.
	 *
	 * @return a Connection object to the database.
	 * @throws ClassNotFoundException if the MySQL JDBC driver class is not found.
	 * @throws SQLException           if a database access error occurs.
	 */
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
	}
}
