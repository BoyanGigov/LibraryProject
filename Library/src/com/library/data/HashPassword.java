package com.library.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class HashPassword {

	public static String hashPassword(String password) throws NoSuchAlgorithmException {
		// create object digest that has algorithm SHA-256
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		// use digest method on the object to convert String password that is in UTF8
		// format into byte code
		byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		// decode the byte code back to string in order to get the HEX code, i.e the
		// location the String is contained; Works only with Java8 (or newer?)
		String passwordHash = Base64.getEncoder().encodeToString(hash);

		return passwordHash;
	}
}
