package com.library.service.admin;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.library.entity.Librarian;

import tools.jackson.databind.JsonNode;

public interface AdminLibrarianService {
	public Librarian saveLibrarian(Librarian libr) ;
	
	public Optional<Librarian> findById(Long libr) ;

	public void deleteLibrarian(Librarian libr) ;


	public Page<Librarian> listAll(JsonNode jsonNode) ;

	public Page<Librarian> findAll(JsonNode jsonNode) ;
	
	

}
