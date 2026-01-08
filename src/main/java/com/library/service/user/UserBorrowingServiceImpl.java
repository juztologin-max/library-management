package com.library.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.PaginatedCriteriaBuilder;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting; // Ensure this is from the jakarta-compatible API
import com.library.entity.Book;
import com.library.entityviews.BookBorrowingView;
import com.library.repository.user.UserBorrowingRepository;

import jakarta.persistence.EntityManager;
import tools.jackson.databind.JsonNode;

@Service
public class UserBorrowingServiceImpl implements UserBorrowingService {
	@Autowired
	private UserBorrowingRepository repo;
	@Autowired
	private EntityViewManager evm;
	@Autowired
	private CriteriaBuilderFactory cbf;
	@Autowired
	private EntityManager em;

	public Page<BookBorrowingView> listAll(JsonNode jsonNode) {
		List<Sort.Order> orders = new ArrayList<>();
		int pageNo = jsonNode.get("pageable").get("pageNo").asInt();
		int limit = jsonNode.get("pageable").get("pageSize").asInt();
		int userId = jsonNode.get("userid").asInt();
		JsonNode sortableNode = jsonNode.path("pageable").get("sortable");
		for (Entry<String, JsonNode> entry : sortableNode.properties()) {
			String column = entry.getKey();

			Sort.Direction direction = entry.getValue().asString().equalsIgnoreCase("ASC") ? Sort.Direction.ASC
					: Sort.Direction.DESC;
			orders.add(new Sort.Order(direction, column));

		}

		Pageable pageable = PageRequest.of(pageNo, limit, Sort.by(orders));
		CriteriaBuilder<Book> cb = cbf.create(em, Book.class);

		EntityViewSetting<BookBorrowingView, PaginatedCriteriaBuilder<BookBorrowingView>> setting = EntityViewSetting
				.create(BookBorrowingView.class, (int) pageable.getOffset(), pageable.getPageSize());

		setting.addOptionalParameter("userId", userId);

		PaginatedCriteriaBuilder<BookBorrowingView> pcb = evm.applySetting(setting, cb);

		PagedList<BookBorrowingView> pagedList = pcb.getResultList();

		return new PageImpl<>(pagedList, pageable, pagedList.getTotalSize());

	}

}
