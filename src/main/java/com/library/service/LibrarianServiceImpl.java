package com.library.service;

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
import com.library.entity.Librarian;
import com.library.repository.LibrarianRepository;

import tools.jackson.databind.JsonNode;

@Service
public class LibrarianServiceImpl implements LibrarianService {
	@Autowired
	private LibrarianRepository repo;
	@Autowired
	private SearchSpecification<Librarian> spec;
	
	public Librarian saveLibrarian(Librarian libr) {
		return repo.save(libr);
	}

	public void deleteLibrarian(Librarian libr) {
		repo.delete(libr);
	}


	

	public Page<Librarian> listAll(JsonNode jsonNode) {
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

	public Page<Librarian> findAll(JsonNode jsonNode) {
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
}
