package com.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_roles")
public class LoginRoles {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="login_role_id")
	private long id;
	@Column(nullable = false, unique = true, length = 30)
	private String roleName;

	public LoginRoles() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String role) {
		this.roleName = role;
	}

}
