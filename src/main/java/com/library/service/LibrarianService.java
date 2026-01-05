package com.library.service;

import org.springframework.data.domain.Page;

import com.library.entity.Librarian;

import tools.jackson.databind.JsonNode;

public interface LibrarianService {
	public Librarian saveLibrarian(Librarian libr) ;

	public void deleteLibrarian(Librarian libr) ;


	public Page<Librarian> listAll(JsonNode jsonNode) ;

	public Page<Librarian> findAll(JsonNode jsonNode) ;
	
	

}
