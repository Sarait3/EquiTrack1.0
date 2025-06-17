package Entities;

public class User {
	int id;
	String fName, lName, email, password, role;

	User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(int id, String role, String fName, String lName, String email, String password) {
		super();
		this.id = id;
		this.role = role;
		this.fName = fName;
		this.lName = lName;
		this.email = email;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
