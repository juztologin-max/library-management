package com.library.service.librarian;

import org.springframework.data.domain.Page;

import com.library.projections.librarian.LibrarianBorrowingProjection;

import tools.jackson.databind.JsonNode;

public interface LibrarianBorrowingService {


	public Page<LibrarianBorrowingProjection> listAllBorrwings(JsonNode jsonNode);

	public Page<LibrarianBorrowingProjection> findAll(JsonNode jsonNode);

	void acceptBorrowingOrReturning(Long borrowingId);



	

}
