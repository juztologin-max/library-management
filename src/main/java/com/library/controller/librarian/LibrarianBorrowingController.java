package com.library.controller.librarian;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Book;
import com.library.service.admin.AdminUserService;
import com.library.service.librarian.LibrarianBorrowingService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/user/api/manage-borrowing")
@RestController
public class LibrarianBorrowingController {
	@Autowired
	private LibrarianBorrowingService librarianBorrowingService;

	@Autowired
	private AdminUserService userService;

	@PostMapping("/list-borrowings")
	public PagedModel<Book> listBorrowings(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianBorrowingService.listAllBorrwings(payload));

	}

	@PostMapping("/search-borrowings")
	public PagedModel<Book> getListOfBorrwingsMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(librarianBorrowingService.findAll(payload));

	}

	
}
