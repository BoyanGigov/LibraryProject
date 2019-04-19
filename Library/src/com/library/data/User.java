package com.library.data;

import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
public class User {

	public enum UserTypeEnum {DEFAULT, STANDARD, ADMIN};
	@XmlElement
	private UserTypeEnum userType;
	@XmlElement
	private String username;
	@XmlElement
	private String passHash;
	@XmlElement
	private String passSalt;

	public User() {

	}

	public User(UserTypeEnum userType, String username, String password) throws IllegalArgumentException, NoSuchAlgorithmException {
		this.userType = userType;
		this.username = username;
		passHash = this.hashPassword(password, true);
	}

	// userType getter
	public UserTypeEnum getUserType() {
		return this.userType;
	}

	// username getter
	public String getUsername() {
		return this.username;
	}

	// password-related methods
	protected String hashPassword(String password, boolean newPass) throws NoSuchAlgorithmException {
		if (newPass) {
			password = saltPass(password);
		}
		return HashPassword.hashPassword(password);
	}

	protected String getPasswordHash() {
		return this.passHash;
	}

	protected String saltPass(String password) {
		String[] passAndSalt = SaltPassword.saltPassword(password);
		this.passSalt = passAndSalt[1];
		return passAndSalt[0];
	}

	protected String getPasswordSalt() {
		return this.passSalt;
	}

}
