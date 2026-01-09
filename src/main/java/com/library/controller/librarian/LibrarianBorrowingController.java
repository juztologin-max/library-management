package com.library.controller.librarian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.projections.librarian.LibrarianBorrowingProjection;
import com.library.service.librarian.LibrarianBorrowingService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/librarian/api/manage-borrowing")
@RestController
public class LibrarianBorrowingController {
	@Autowired
	private LibrarianBorrowingService librarianBorrowingService;

	

	@PostMapping("/list-borrowings")
	public PagedModel<LibrarianBorrowingProjection> listBorrowings(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianBorrowingService.listAllBorrwings(payload));

	}

	@PostMapping("/search-borrowings")
	public PagedModel<LibrarianBorrowingProjection> getListOfBorrwingsMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianBorrowingService.findAll(payload));

	}

	
}
