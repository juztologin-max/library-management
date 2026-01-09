package com.library.service.user;

import org.springframework.data.domain.Page;

import com.library.entity.Book;
import com.library.entity.User;
import com.library.projections.BookBorrowableReturnableStatus;

import jakarta.transaction.Transactional;
import tools.jackson.databind.JsonNode;

public interface UserBorrowingService {

	@Transactional
	public void borrowBook(User user,Long bookId);
	
	
	
	public BookBorrowableReturnableStatus isBorrowableReturnableByUser(Long userId, Long bookId);

	public Page<Book> listAllBooks(JsonNode jsonNode);

	public Page<Book> findAll(JsonNode jsonNode);



	void returnBook(User user, Long bookId);

}
