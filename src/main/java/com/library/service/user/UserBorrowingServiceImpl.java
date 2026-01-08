package com.library.service.user;

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
import com.library.repository.user.UserBookRepository;

import tools.jackson.databind.JsonNode;

@Service
public class UserBorrowingServiceImpl implements UserBorrowingService {
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
			
			Sort.Direction direction=entry.getValue().asString().equalsIgnoreCase("ASC")?Sort.Direction.ASC:Sort.Direction.DESC;
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
			Sort.Direction direction=entry.getValue().asString().equalsIgnoreCase("ASC")?Sort.Direction.ASC:Sort.Direction.DESC;
		
			
			orders.add(new Sort.Order(direction, column));
			
		}
		Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(orders));

		spec.setJsonNode(jsonNode.get("searchable"));
		return bookRepo.findAll(spec, pageable);
		
	}
	

	
}
