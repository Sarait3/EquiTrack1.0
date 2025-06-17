package DatabaseAccess;

import java.sql.*;
import java.util.UUID;

import Entities.User;

public class DBAccessor {
	private static final String connString = "jdbc:mysql://localhost/EquiTrack";
	private static final String dbUsername = "dbUser";
	private static final String dbPassword = "dbPass";

	private static final String userTable = "users";
	private static final String userIDCol = "id";
	private static final String userEmailCol = "email";
	private static final String userFNameCol = "fName";
	private static final String userLNameCol = "lName";
	private static final String userPassCol = "password";
	private static final String userSessionCol = "sessionID";


	static Connection conn = null;

	public static User getUser(String columnName, String columnData) {
		String sql = String.format("SELECT * FROM users WHERE %s like ?;", columnName);
		int id = 0;
		String fName = null, 
				lName = null, 
				email = null, 
				password = null,
				sessionID = null;

		try {

			conn = DriverManager.getConnection(connString, dbUsername, dbPassword);


			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, columnData);
			ResultSet results = statement.executeQuery();


			while (results.next()) {
				id = results.getInt("id");
				fName = results.getString("fName");
				lName = results.getString("lname");
				email = results.getString("email");
				password = results.getString("password");
				sessionID = results.getString("sessionID");
			}

			results.close();
			statement.close();
			conn.close();


			return new User(id, fName, lName, email, password, sessionID);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void writeSessionToDatabaseByID(int userID, UUID sessionID) {
		String sql = "UPDATE users"
				+ "SET sessionID = ?"
				+ "WHERE id = ?";

		try {
			conn = DriverManager.getConnection(connString, dbUsername, dbPassword);

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, sessionID.toString());
			statement.setInt(2, userID);
		} catch (Exception e) {

		}


	}

}
