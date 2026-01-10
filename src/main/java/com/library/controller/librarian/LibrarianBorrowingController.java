package com.library.controller.librarian;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/accept/{id}")
	public Map<String, Boolean>  acceptBorrowingOrReturning(@PathVariable("id") Long borrowingId) {
		Map<String, Boolean> ret = new HashMap<>();
		boolean status=false;
		try {
			librarianBorrowingService.acceptBorrowingOrReturning(borrowingId);
			status=true;
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		ret.put("successfull", status);
		return ret;
	}
	
	@GetMapping("/delete/{id}")
	public Map<String, Boolean>  deleteBorrowing(@PathVariable("id") Long borrowingId) {
		Map<String, Boolean> ret = new HashMap<>();
		boolean status=false;
		try {
			librarianBorrowingService.deleteBorrowing(borrowingId);
			status=true;
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		ret.put("successfull", status);
		return ret;
	}
}
