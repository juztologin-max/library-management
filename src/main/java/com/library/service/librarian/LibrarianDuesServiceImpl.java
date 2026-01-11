package com.library.service.librarian;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.hibernate.query.common.TemporalUnit;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
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
import com.library.projections.librarian.LibrarianDuesProjection;
import com.library.repository.librarian.LibrarianDuesRepository;
import com.library.settings.DuesSettings;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import tools.jackson.databind.JsonNode;

@Service
public class LibrarianDuesServiceImpl implements LibrarianDuesService {
	@Autowired
	private LibrarianDuesRepository repo;
	@Autowired
	private ConversionService conversionService;
	@Autowired
	DuesSettings duesSetting;

	/*public Page<LibrarianDuesProjection> listAll(JsonNode jsonNode) {
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
		System.out.println(duesSetting.borrowDuration());
		return repo.getDues(pageable, duesSetting.borrowDuration());
	}*/

	public Page<LibrarianDuesProjection> listAll(JsonNode jsonNode) {
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
		//Specification<Borrowing> spec = new SearchSpecification<>(jsonNode.get("searchable"), conversionService);
		Specification<Borrowing> due =dueAndStatusLimits(duesSetting.borrowDuration());
		return repo.findBy(due, q -> q.as(LibrarianDuesProjection.class).page(pageable));

	}
	
	public Page<LibrarianDuesProjection> findAll(JsonNode jsonNode) {
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
		Specification<Borrowing> spec = new SearchSpecification<>(jsonNode.get("searchable"), conversionService);
		Specification<Borrowing> due =dueAndStatusLimits(duesSetting.borrowDuration());
		return repo.findBy(spec.and(due), q -> q.as(LibrarianDuesProjection.class).page(pageable));

	}

	public static Specification<Borrowing> dueAndStatusLimits(Long dueLimit) {
		return (root, query, cb) -> {
			LocalDateTime limitDate=LocalDateTime.now().minusDays(dueLimit);
					
			Predicate duePredicate = cb.lessThanOrEqualTo(root.get("borrowDate"),limitDate);
			CriteriaBuilder.In<String> inStatus = cb.in(root.get("status"));
			inStatus.value("BORROW_ACCEPTED").value("RETURN").value("BORROW");
			return cb.and(duePredicate,inStatus);
		};

	}

}
