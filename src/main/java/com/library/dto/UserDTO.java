package com.library.dto;

/**
 * DTO class to shuttle user info between login page and
 * 
 * @author Ashwin
 * @version 0.1
 */
public class UserDTO {
	private String name;
	private String password;

	public UserDTO() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
