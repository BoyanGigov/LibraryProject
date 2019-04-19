package com.library.data;


public abstract class SaltPassword {
	
	private static final int SALT_LENGTH = 10;
	

	public static String[] saltPassword(String password) {
		String salt = String.valueOf(Math.random()*Math.pow(10, SALT_LENGTH));
		
		String[] returnResult = {password.concat(salt), salt};
		return returnResult;
	}
	
}
