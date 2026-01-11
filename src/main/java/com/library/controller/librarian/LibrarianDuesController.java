package com.library.controller.librarian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.projections.librarian.LibrarianDuesProjection;
import com.library.service.librarian.LibrarianDuesService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/librarian/api/manage-dues")
@RestController
public class LibrarianDuesController {
	@Autowired
	private LibrarianDuesService librarianDuesService;

	

	@PostMapping("/list-dues")
	public PagedModel<LibrarianDuesProjection> listBorrowings(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianDuesService.listAll(payload));

	}
	@PostMapping("/search-dues")
	public PagedModel<LibrarianDuesProjection> getListOfBorrwingsMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianDuesService.findAll(payload));
	}

	}
