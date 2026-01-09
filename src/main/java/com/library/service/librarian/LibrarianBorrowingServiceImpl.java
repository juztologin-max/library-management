package com.library.service.librarian;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.library.component.SearchSpecification;
import com.library.entity.Book;
import com.library.entity.Borrowing;
import com.library.entity.User;
import com.library.other.BookBorrowableReturnableStatus;
import com.library.other.BorrowStatus;
import com.library.repository.user.UserBookRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.JsonNode;

@Service
public class LibrarianBorrowingServiceImpl implements LibrarianBorrowingService {
	@Autowired
	private UserBookRepository bookRepo;
	@Autowired
	private SearchSpecification<Book> spec;

	public Page<Book> listAllBooks(JsonNode jsonNode) {
		List<Sort.Order> orders = new ArrayList<>();
		int pageNo = jsonNode.get("pageable").get("pageNo").asInt();
		int limit = jsonNode.get("pageable").get("pageSize").asInt();
		JsonNode sortableNode = jsonNode.path("pageable").get("sortable");
		for (Entry<String, JsonNode> entry : sortableNode.properties()) {
			String column = entry.getKey();

			Sort.Direction direction = entry.getValue().asString().equalsIgnoreCase("ASC") ? Sort.Direction.ASC
					: Sort.Direction.DESC;
			orders.add(new Sort.Order(direction, column));

		}

		Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(orders));
		return bookRepo.findAll(pageable);
	}

	public Page<Book> findAll(JsonNode jsonNode) {
		List<Sort.Order> orders = new ArrayList<>();
		int pageNo = jsonNode.get("pageable").get("pageNo").asInt();
		int limit = jsonNode.get("pageable").get("pageSize").asInt();
		JsonNode sortableNode = jsonNode.path("pageable").get("sortable");
		for (Entry<String, JsonNode> entry : sortableNode.properties()) {
			String column = entry.getKey();
			Sort.Direction direction = entry.getValue().asString().equalsIgnoreCase("ASC") ? Sort.Direction.ASC
					: Sort.Direction.DESC;

			orders.add(new Sort.Order(direction, column));

		}
		Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(orders));

		spec.setJsonNode(jsonNode.get("searchable"));
		return bookRepo.findAll(spec, pageable);

	}
	
	public BookBorrowableReturnableStatus isBorrowableReturnableByUser(Long userId, Long bookId) {
		BookBorrowableReturnableStatus temp =bookRepo.getbookBorrowableReturnableStatus(userId, bookId);
		System.out.println(temp.isBorrowbleByUser());
		System.out.println(temp.isReturnableByUser());
		return temp;
	}

	@Override
	@Transactional
	public void borrowBook(User user,Long bookId) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		Borrowing borrowing = new Borrowing();
		borrowing.setUser(user);
		borrowing.setBook(book);
		borrowing.setBorrowDate(LocalDateTime.now());
		borrowing.setStatus(BorrowStatus.BORROW);
		book.addBorrowing(borrowing);
		bookRepo.save(book);
	}
	
	
	@Override
	@Transactional
	public void returnBook(User user,Long bookId) {
		Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		Borrowing borrowing = book.getBorrowingOfUser(user.getId()).get();
		borrowing.setStatus(BorrowStatus.RETURN);
		book.updateBorrowingOfUser(borrowing);
		bookRepo.save(book);
	}

}
