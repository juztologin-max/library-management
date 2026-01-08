package com.library.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedModel;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Book;
import com.library.entity.Librarian;
import com.library.entity.LoginUser;
import com.library.entity.LoginUserDetails;
import com.library.service.BookService;

import jakarta.validation.Valid;
import tools.jackson.databind.JsonNode;

@RequestMapping("/admin/api/manage-book")
@RestController
public class ManageBookController {
	@Autowired
	private BookService bookService;

	@PostMapping("/save")
	public Map<String, Boolean> saveBook(@RequestBody JsonNode jsonNode, @AuthenticationPrincipal UserDetails usr) {
		Map<String, Boolean> ret = new HashMap<>();
		ret.put("successfull", false);

		Book book = new Book();
		book.setName(jsonNode.get("name").asString());
		book.setAuthor(jsonNode.get("author").asString());
		book.setPublisher(jsonNode.get("publisher").asString());
		System.out.println(jsonNode.get("publishedDate").asString());
		book.setPublishedAt(jsonNode.get("publishedDate").asString());
		book.setTotal(jsonNode.get("total").asLong());
		book.setDescription(jsonNode.get("description").asString());
		System.out.println(jsonNode.get("content").asString());
		book.setContent(Base64.getDecoder().decode(jsonNode.get("content").asString().split(",")[1]));
		book.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
		if (book.getId() == null) {
			book.setCreatedBy(((LoginUserDetails) usr).getUser());
		}
		book.setUpdatedBy(((LoginUserDetails) usr).getUser());
		try {
			book = bookService.saveBook(book);
		} catch (Exception ex) {
			ret.put("successfull", false);
		}
		ret.put("successfull", book.getId() != null);
		return ret;
	}

	@PostMapping("/list")
	public PagedModel<Book> getListOfBooks(@RequestBody JsonNode payload) {
		return new PagedModel<>(bookService.listAll(payload));

	}

	@PostMapping("/search")
	public PagedModel<Book> getListOfBooksMatching(@RequestBody JsonNode payload) {
		return new PagedModel<>(bookService.findAll(payload));

	}

	@PutMapping("/{id}")
	@Valid
	public Map<String, Boolean> updateBook(@Valid @PathVariable Long id, @RequestBody JsonNode jsonNode,
			@AuthenticationPrincipal UserDetails usr) {
		Book book = bookService.findById(id).get();
		Map<String, Boolean> ret = new HashMap<>();
		ret.put("successfull", false);
		if (book != null) {
			book.setName(jsonNode.get("name").asString());
			System.out.println(book.getName());
			book.setAuthor(jsonNode.get("author").asString());
			book.setPublisher(jsonNode.get("publisher").asString());
			book.setPublishedAt(jsonNode.get("publishedDate").asString());
			book.setTotal(jsonNode.get("total").asLong());
			book.setDescription(jsonNode.get("description").asString());
			book.setContent(Base64.getDecoder().decode(jsonNode.get("content").asString()));
			book.setUpdatedBy(((LoginUserDetails) usr).getUser());
			try {
				book = bookService.saveBook(book);
			} catch (Exception ex) {
				ret.put("successfull", false);
			}
			ret.put("successfull", book.getId() != null);

		}
		return ret;

	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deleteLibrarian(@Valid @PathVariable Long id) {
		boolean status = true;
		Book book = new Book();
		book.setId(id);
		Map<String, Boolean> ret = new HashMap<>();
		try {
			bookService.deleteBook(book);
		} catch (Exception ex) {
			status = false;
		}
		ret.put("successfull", status);
		return ret;

	}

}
