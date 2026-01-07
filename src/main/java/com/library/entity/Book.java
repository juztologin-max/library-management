package com.library.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	@Column(name = "cover_page",columnDefinition = "MEDIUMBLOB")
	private byte[] content;

	@Column(nullable = false)
	private Long total;

	@Column(nullable = false)
	private LocalDate publishedAt;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public LocalDate getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDate publishedAt) {
		this.publishedAt = publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = LocalDate.parse(publishedAt);
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
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

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

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
