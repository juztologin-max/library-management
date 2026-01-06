package com.library.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

/**
 * DTO class to shuttle user info between login page and
 * 
 * @author Ashwin
 * @version 0.1
 */
@Entity
@Table(name = "librarians")
public final class Librarian {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "librarian_id")
	private Long id;

	@Column(nullable = false, length = 30)
	private String legalName;

	@Column(nullable = false, length = 100)
	private String email;

	@Column(nullable = false, length = 20)
	private String phoneNo;

	@Column(nullable = false, length = 200)
	private String address;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creater_login_user_id", nullable = false, updatable = false) // foreign key
	private LoginUser createdBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updater_login_user_id", nullable = false) // foreign key
	private LoginUser updatedBy;

	// many to one
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "login_user_id") // foreign key
	private LoginUser loginUser;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		
	}

	public Librarian() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LoginUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LoginUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(LoginUser createdBy) {
		this.createdBy = createdBy;
	}

	public LoginUser getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(LoginUser updatedBy) {
		this.updatedBy = updatedBy;
	}

}
