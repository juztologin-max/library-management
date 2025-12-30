package com.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank; 
/**
 * DTO class to shuttle user info between login page and
 * 
 * @author Ashwin
 * @version 0.1
 */
@Entity
@Table(name = "login_users")
public final class LoginUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_user_id")
	private Long id;
	

	@Column(nullable = false, unique = true, length = 30)
	
	private String name;
	@Column(nullable = false, length = 100)
	private String password;
	@Column(nullable = false)
	boolean enabled;
	

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	//many to one
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="login_role_id") //foreign key
	private LoginRoles role;
		
	

	public LoginUser() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LoginRoles getRole() {
		return role;
	}

	public void setRole(LoginRoles role) {
		this.role = role;
	}
	
	

}
