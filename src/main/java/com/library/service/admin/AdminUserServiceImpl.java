package com.library.service.admin;

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
import com.library.repository.admin.AdminUserRepository;

import tools.jackson.databind.JsonNode;

@Service
public class AdminUserServiceImpl implements AdminUserService {
	@Autowired
	private AdminUserRepository repo;
	@Autowired
	private ConversionService conversionService;
	
	
	public User saveUser(User usr) {
		return repo.save(usr);
	}

	public void deleteUser(User usr) {
		repo.delete(usr);
	}


	

	public Page<User> listAll(JsonNode jsonNode) {
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

	public Page<User> findAll(JsonNode jsonNode) {
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

		Specification<User> spec = new SearchSpecification<>(jsonNode.get("searchable"),conversionService);
		return repo.findAll(spec, pageable);
		
	}
	
	public Optional<User> findById(Long id) {
		return repo.findById(id);
	}
	
	public Optional<User> findByLoginUser(LoginUser lusr) {
		return repo.findByLoginUser(lusr);
	}
	
	
}
