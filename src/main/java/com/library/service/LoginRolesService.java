package com.library.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.entity.LoginRoles;
import com.library.repository.LoginRolesRepository;
@Service
public class LoginRolesService {
	@Autowired
	private  LoginRolesRepository repo;

	public LoginRoles saveLoginUser(LoginRoles role) {
		return repo.save(role);
	}
	
	public Optional<LoginRoles> findById(long id) {
		return repo.findById(id);
	}
	
	public Optional<LoginRoles> findByName(String name) {
		return repo.findByRoleName( name);
	}
	
}
