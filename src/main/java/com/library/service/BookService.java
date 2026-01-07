package com.library.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.library.entity.Book;
import com.library.entity.Librarian;

import tools.jackson.databind.JsonNode;

public interface BookService {
	public Book saveLibrarian(Book book) ;
	
	public Optional<Book> findById(Long id) ;

	public void deleteLibrarian(Book book) ;


	public Page<Book> listAll(JsonNode jsonNode) ;

	public Page<Book> findAll(JsonNode jsonNode) ;
	
	

}
