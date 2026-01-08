package com.library.service.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.library.component.SearchSpecification;
import com.library.entity.Book;
import com.library.repository.admin.AdminBookRepository;

import tools.jackson.databind.JsonNode;

@Service
public class AdminBookServiceImpl implements AdminBookService {
	@Autowired
	private AdminBookRepository repo;
	@Autowired
	private SearchSpecification<Book> spec;
	
	public Book saveBook(Book book) {
		return repo.save(book);
	}

	public void deleteBook(Book book) {
		repo.delete(book);
	}


	

	public Page<Book> listAll(JsonNode jsonNode) {
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
		return repo.findAll(pageable);
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
		return repo.findAll(spec, pageable);
		
	}
	
	public Optional<Book> findById(Long id) {
		return repo.findById(id);
	}
	
}
