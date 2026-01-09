package com.library.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.library.other.BorrowStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "borrowing")
public final class Borrowing {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "borrowing_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private BorrowStatus status;

	@Column(nullable = false)
	private LocalDateTime borrowDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "book_id", nullable = false, updatable = false) // foreign key
	private Book book;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false, updatable = false) // foreign key
	private User user;

	public Borrowing() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BorrowStatus getStatus() {
		return status;
	}

	public void setStatus(BorrowStatus status) {
		this.status = status;
	}

	public LocalDateTime getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDateTime borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
