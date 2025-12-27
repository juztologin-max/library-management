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
	private boolean enabled=false;
	private String role;

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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.enabled = isEnabled;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
