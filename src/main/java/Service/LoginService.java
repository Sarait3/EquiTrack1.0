package Service;

import DatabaseAccess.DBReader;
import Entities.User;

public class LoginService {

	public boolean validateLogin(String email, String password) {

		User user = DBReader.getUser(email);

		try {
			if (user.getPassword().equals(password)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;	
	}

}
