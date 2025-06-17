package Service;

import java.util.UUID;

import DatabaseAccess.DBAccessor;
import Entities.User;

public class LoginService {
	
	private String errormsg = "";
	
	public String loginPage(boolean loginFailed) {
		
		errormsg = loginFailed ? "" : "<p>Login failed, please try again</p>";

		String page = String.format("<html>"
		+ "<head title=\"EquiTrack\">"
		+ "<style>p {color: red;}</style>"
		+ "<body>"
		+ "<h1>Login</h1>"
		+ "<form method=\"post\">"
		+ "<label for=\"email\">Email:</label><br>"
		+ "<input type=\"text\" name=\"email\" id=\"email\" required><br>"
		+ "<label for=\"password\">Password:</label><br>"
		+ "<input type=\"password\" name=\"password\" id=\"password\" required><br>"
		+ "<input type=\"submit\" value=\"Login\"><br>"
		+ "%s"
		+ "</form>"
		+ "</body>"
		+ "</head>"
		+ "</html>",
		errormsg
		);
		
		return page;
		
	}

	public UUID validateLogin(String email, String password) {

		User user = DBAccessor.getUser("email", email);

		try {
			if (user.getPassword().equals(password)) {
				return assignSessionID(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;	
	}
	
	public boolean validateSessionID(UUID sessionID) {
		return DBAccessor.getUser("sessionID", sessionID.toString()) != null;
	}
	
	private UUID assignSessionID(User user) {
		
		UUID sessionID = UUID.randomUUID();
		
		DBAccessor.writeSessionToDatabaseByID(user.getId(), sessionID);
		
		return sessionID;
	}
}
