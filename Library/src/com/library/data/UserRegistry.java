package com.library.data;

import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.library.data.User.UserTypeEnum;
import com.library.exceptions.LoginException;
import com.library.exceptions.UserException;
import com.library.xml.userRegistry.StringUserAdapter;
import com.library.xml.userRegistry.UserMapXML;

@XmlRootElement(name = "UserReg")
public class UserRegistry implements IRegistry{

	@XmlJavaTypeAdapter(StringUserAdapter.class)
	private Map<String, User> userRegistry = new TreeMap<String, User>();

// default constructor for XML
	public UserRegistry() {

	}
	
	@Override
	public Iterator<String> getKeySetIter() {
		return userRegistry.keySet().iterator();
	}

	public void add(UserTypeEnum userType, String username, String password) throws LoginException {
		if (checkIfUsernameAvailable(username)) {
			User tempUser;
			try {
				tempUser = new User(userType, username, password);
				this.userRegistry.put(username, tempUser);
			} catch (IllegalArgumentException e) {
				System.out.println("\n\tError UR1");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("\n\tError UR2");
			}
		} else {
			throw new LoginException("\tUSERNAME ALREADY TAKEN");
		}
		System.out.println("\n\tNew user created successfully.\n");
		UserMapXML.writeToXML(this);
	}

	public boolean checkIfUsernameAvailable(String username) {
		return !this.userRegistry.containsKey(username);
	}

	private boolean confirmPass(String username, String password) {
		User tempUser = userRegistry.get(username);
		String hashPass;
		try {
			hashPass = tempUser.hashPassword(password + tempUser.getPasswordSalt(), false);
			if (hashPass.equals(tempUser.getPasswordHash())) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println("\n\tError UR3");
			return false;
		} catch (Error e) {
			System.out.println("\n\tError UR4");
			return false;
		}
	}

	public UserTypeEnum logIn(String username, String password) throws LoginException {
		if (this.userRegistry.get(username) == null) {
			throw new LoginException("\tINCORRECT USERNAME");
		}
		if (confirmPass(username, password)) {
			return userRegistry.get(username).getUserType();
		} else
			throw new LoginException("\tINCORRECT PASSWORD");
	}

	public void remove(String username, String password) throws UserException {
		if (checkIfUsernameAvailable(username)) {
			throw new UserException("\tUSER DOES NOT EXIST");
		} else {
			if (confirmPass(username, password)) {
				this.userRegistry.remove(username);
				System.out.println("\n\t user deleted\n");
				UserMapXML.writeToXML(this);
			} else {
				throw new UserException("\tWRONG PASSWORD");
			}
		}
	}

	public void removeByAdmin(String username) throws UserException {
		if (!userRegistry.containsKey(username)) {
			throw new UserException("\tUSER DOES NOT EXIST");
		}
		this.userRegistry.remove(username);
		System.out.println("\n\t user deleted\n");
		UserMapXML.writeToXML(this);
	}
	
	public User getUser(String username) throws UserException {
		try {
		return userRegistry.get(username);
		} catch (NullPointerException e) {
			throw new UserException("\tUser not found");
		}
	}
	
	public Iterator<User> getUsersIter() {
		return this.userRegistry.values().iterator();
	}
}
