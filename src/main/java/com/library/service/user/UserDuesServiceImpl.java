package com.library.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.library.entity.LoginUserDetails;
import com.library.projections.librarian.LibrarianDuesProjection;
import com.library.repository.user.UserDuesRepository;
import com.library.service.admin.AdminUserService;
import com.library.settings.DuesSettings;

import tools.jackson.databind.JsonNode;

@Service
public class UserDuesServiceImpl implements UserDuesService {
	@Autowired
	private UserDuesRepository repo;
	@Autowired
	private ConversionService conversionService;
	@Autowired
	DuesSettings duesSetting;
	@Autowired
	private AdminUserService userService;

	public Page<LibrarianDuesProjection> listAll(JsonNode jsonNode,Long usrId) {
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
		return repo.getDues(pageable, duesSetting.borrowDuration(),usrId);
	}


}
