package com.library.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.library.dto.TableRequest;
import com.library.entity.LoginUser;
import com.library.repository.LoginUserRepository;

@Service
public class LoginUserService {
	@Autowired
	private LoginUserRepository repo;

	public LoginUser saveLoginUser(LoginUser usr) {
		return repo.save(usr);
	}

	public Optional<LoginUser> findById(long id) {
		return repo.findById(id);
	}

	public Optional<LoginUser> findByName(String name) {
		System.out.println(name);
		return repo.findByName(name);
	}

	public List<LoginUser> list() {
		return repo.findAll();
	}

	public List<LoginUser> listAll(TableRequest table) {
		List<Sort.Order> orders = new ArrayList<>();
		for (String column : table.getSortables().keySet()) {
			if (table.getSortables().get(column).equals("ASC")) {
				orders.add(new Sort.Order(Sort.Direction.ASC, column));
			} else {
				orders.add(new Sort.Order(Sort.Direction.DESC, column));
			}
		}
		Pageable pageable = PageRequest.of(table.getStart(), table.getLimit(), Sort.by(orders));
		return repo.findBy(pageable);
	}
}
