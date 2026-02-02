package com.equitrack.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {

        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String db   = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");

        if (port == null || port.isEmpty()) {
            port = "3306";
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db +
                "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        return DriverManager.getConnection(url, user, pass);
    }
}


/**
 * DBConnection is a utility class that provides a method to establish a
 * connection to the EquiTrack MySQL database.
 
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
	 
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(connString, dbUsername, dbPassword);
	}
}
*/