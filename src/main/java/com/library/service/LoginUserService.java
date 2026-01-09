package com.library.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.library.component.SearchSpecification;

import com.library.entity.LoginUser;
import com.library.entity.User;
import com.library.repository.LoginUserRepository;

import tools.jackson.databind.JsonNode;

@Service
public class LoginUserService {
	@Autowired
	private LoginUserRepository repo;
	@Autowired
	private ConversionService conversionService;
	
	public LoginUser saveLoginUser(LoginUser usr) {
		return repo.save(usr);
	}

	public void deleteLoginUser(LoginUser usr) {
		repo.delete(usr);
	}

	public Optional<LoginUser> findById(long id) {
		return repo.findById(id);
	}

	public Optional<LoginUser> findByName(String name) {
		return repo.findByName(name);
	}

	public Optional<LoginUser> findByNameEnabled(String name) {
		LoginUser usr = repo.findByName(name).get();
		if (!usr.isEnabled())
			usr = null;
		return Optional.ofNullable(usr);
	}

	public List<LoginUser> list() {
		return repo.findAll();
	}

	public Page<LoginUser> listAll(JsonNode jsonNode) {
		List<Sort.Order> orders = new ArrayList<>();
		int pageNo = jsonNode.get("pageable").get("pageNo").asInt();
		int limit = jsonNode.get("pageable").get("pageSize").asInt();
		JsonNode sortableNode = jsonNode.path("pageable").get("sortable");
		for (Entry<String, JsonNode> entry : sortableNode.properties()) {
			String column = entry.getKey();
			if (entry.getValue().asString().equals("ASC")) {
				orders.add(new Sort.Order(Sort.Direction.ASC, column));
			} else {
				orders.add(new Sort.Order(Sort.Direction.DESC, column));
			}
		}
		Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(orders));
		return repo.findBy(pageable);
	}

	public Page<LoginUser> findAll(JsonNode jsonNode) {
		List<Sort.Order> orders = new ArrayList<>();
		int pageNo = jsonNode.get("pageable").get("pageNo").asInt();
		int limit = jsonNode.get("pageable").get("pageSize").asInt();
		JsonNode sortableNode = jsonNode.path("pageable").get("sortable");
		for (Entry<String, JsonNode> entry : sortableNode.properties()) {
			String column = entry.getKey();
			if (entry.getValue().asString().equals("ASC")) {
				orders.add(new Sort.Order(Sort.Direction.ASC, column));
			} else {
				orders.add(new Sort.Order(Sort.Direction.DESC, column));
			}
		}
		Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(orders));

		Specification<LoginUser> spec = new SearchSpecification<>(jsonNode.get("searchable"),conversionService
		);
		return repo.findAll(spec, pageable);
	}
}
