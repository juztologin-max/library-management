package com.library.service.librarian;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.library.component.SearchSpecification;
import com.library.entity.Borrowing;
import com.library.projections.librarian.LibrarianBorrowingProjection;
import com.library.repository.librarian.LibrarianBorrowingRepository;

import tools.jackson.databind.JsonNode;

@Service
public class LibrarianBorrowingServiceImpl implements LibrarianBorrowingService {
	@Autowired
	private LibrarianBorrowingRepository borrowingRepo;
	@Autowired
	private ConversionService conversionService;
	
	public Page<LibrarianBorrowingProjection> listAllBorrwings(JsonNode jsonNode) {
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
		return borrowingRepo.findAllProjectedBy(pageable);
	}

	public Page<LibrarianBorrowingProjection> findAll(JsonNode jsonNode) {
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
		Specification<Borrowing> spec = new SearchSpecification<>(jsonNode.get("searchable"),conversionService);
	    
	    return borrowingRepo.findAllProjectedBy(spec, pageable);
		
		
	}

}
