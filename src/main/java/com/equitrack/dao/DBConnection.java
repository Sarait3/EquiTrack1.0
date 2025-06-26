package com.equitrack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String connString = "jdbc:mysql://localhost/equitrack";
	private static final String dbUsername = "dbUser";
	private static final String dbPassword = "dbPassword";
	
	private static Connection conn;
	
	public static Connection getConnection() throws SQLException {
		return conn = DriverManager.getConnection(connString, dbUsername, dbPassword);
	}
}
