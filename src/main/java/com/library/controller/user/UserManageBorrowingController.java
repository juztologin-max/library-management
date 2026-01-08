package com.library.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Book;
import com.library.service.user.UserBorrowingService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/user/api/manage-borrowing")
@RestController
public class UserManageBorrowingController {
	@Autowired
	private UserBorrowingService userBorrowingService;

	@PostMapping("/list-books")
	public PagedModel<Book> listBooks(@RequestBody JsonNode payload) {
		return new PagedModel<>(userBorrowingService.listAllBooks(payload));

	}

	@PostMapping("/search-books")
	public PagedModel<Book> getListOfBooksMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(userBorrowingService.findAll(payload));

	}
	
	

}
