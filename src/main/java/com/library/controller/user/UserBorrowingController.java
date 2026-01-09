package com.library.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Book;
import com.library.entity.LoginUserDetails;
import com.library.projections.BookBorrowableReturnableStatus;
import com.library.service.admin.AdminUserService;
import com.library.service.user.UserBorrowingService;

import jakarta.validation.Valid;
import tools.jackson.databind.JsonNode;

@RequestMapping("/user/api/manage-borrowing")
@RestController
public class UserBorrowingController {
	@Autowired
	private UserBorrowingService userBorrowingService;

	@Autowired
	private AdminUserService userService;

	@PostMapping("/list-books")
	public PagedModel<Book> listBooks(@RequestBody JsonNode payload) {
		return new PagedModel<>(userBorrowingService.listAllBooks(payload));

	}

	@PostMapping("/search-books")
	public PagedModel<Book> getListOfBooksMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(userBorrowingService.findAll(payload));

	}

	@GetMapping("/is-borrowable-and-returnable/{id}")
	@Valid
	public Map<String, Boolean> checkBorrowabilityAndReturnability(@PathVariable("id") Long bookId,
			@AuthenticationPrincipal UserDetails usr) {
		Map<String, Boolean> ret = new HashMap<>();
		LoginUserDetails lusr = (LoginUserDetails) usr;
		boolean status = false;
		BookBorrowableReturnableStatus borrowable = userBorrowingService
				.isBorrowableReturnableByUser(userService.findByLoginUser(lusr.getUser()).get().getId(), bookId);
		if (borrowable != null) {
			ret.put("isborrowable", borrowable.isBorrowbleByUser());
			ret.put("isreturnable", borrowable.isReturnableByUser());
			status = true;
		}

		ret.put("successfull", status);
		return ret;

	}

	@GetMapping("/borrow/{id}")
	@Valid
	public Map<String, Boolean> borrowBook(@PathVariable("id") Long bookId, @AuthenticationPrincipal UserDetails usr) {
		Map<String, Boolean> ret = new HashMap<>();
		LoginUserDetails lusr = (LoginUserDetails) usr;
		boolean status = false;
		try {
			userBorrowingService.borrowBook(userService.findByLoginUser(lusr.getUser()).get(), bookId);
			status=true;
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		ret.put("successfull", status);
		return ret;

	}
	
	@GetMapping("/return/{id}")
	@Valid
	public Map<String, Boolean> returnBook(@PathVariable("id") Long bookId, @AuthenticationPrincipal UserDetails usr) {
		Map<String, Boolean> ret = new HashMap<>();
		LoginUserDetails lusr = (LoginUserDetails) usr;
		boolean status = false;
		try {
			userBorrowingService.returnBook(userService.findByLoginUser(lusr.getUser()).get(), bookId);
			status=true;
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		ret.put("successfull", status);
		return ret;

	}

}
