package com.library.service.user;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.library.entity.Book;
import com.library.entity.Librarian;

import tools.jackson.databind.JsonNode;

public interface UserBorrowingService {



	public Page<Book> listAllBooks(JsonNode jsonNode) ;

	public Page<Book> findAll(JsonNode jsonNode) ;
	
	

}
