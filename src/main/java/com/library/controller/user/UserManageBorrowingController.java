package com.library.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.library.entityviews.BookBorrowingView;
import com.library.service.user.UserBorrowingService;

import tools.jackson.databind.JsonNode;

@RequestMapping("/admin/api/manage-borrowing")
@RestController
public class UserManageBorrowingController {
	@Autowired
	UserBorrowingService userBorrowingService;

	@PostMapping("/list")
	public PagedModel<BookBorrowingView> getListOfBooks(@RequestBody JsonNode payload) {
		return new PagedModel<>(userBorrowingService.listAll(payload));

	}

	

}
