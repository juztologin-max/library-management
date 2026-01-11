package com.library.service.librarian;

import org.springframework.data.domain.Page;

import com.library.projections.librarian.LibrarianDuesProjection;

import tools.jackson.databind.JsonNode;

public interface LibrarianDuesService {

	public Page<LibrarianDuesProjection> listAll(JsonNode jsonNode) ;

	
	

}
