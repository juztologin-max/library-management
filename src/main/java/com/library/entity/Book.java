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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public final class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "book_id")
	private Long id;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, length = 200)
	private String description;

	@Column(nullable = false, length = 20)
	private String author;

	@Column(nullable = false, length = 100)
	private String publisher;

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

	@Lob
	@Column(name="cover_page")
	private byte[] content;
	
	@Column(nullable = false)
	private Long total;
	
	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		
	}

	public Book() {
	}

	
}
